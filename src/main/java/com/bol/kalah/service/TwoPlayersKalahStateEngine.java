package com.bol.kalah.service;

import com.bol.kalah.service.exception.InvalidMoveException;
import com.bol.kalah.service.exception.KalahFinishedException;
import com.bol.kalah.service.model.Kalah;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

import static com.bol.kalah.service.model.Kalah.PlayerTurn.PLAYER1;
import static com.bol.kalah.service.model.Kalah.PlayerTurn.PLAYER2;
import static com.bol.kalah.service.model.Kalah.GameState.FINISHED;
import static com.bol.kalah.service.model.Kalah.GameState.RUNNING;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 04/16/2022
 */
@Component
@RequiredArgsConstructor
public class TwoPlayersKalahStateEngine implements KalahStateEngine {

    private final Clock clock;

    /**
     * Moves stones from the specified pit id. This method is not thread safe, should be used in a synchronized environment.
     *
     * @param pitId pit id starting from 1
     * @return Kalah game
     */
    @Override
    public Kalah move(Kalah kalah, int pitId) throws KalahFinishedException, InvalidMoveException {
        // pit id starts from 1, but inside it works from 0
        pitId--;

        // If game is over then throw exception
        if (kalah.getState() == FINISHED) throw new KalahFinishedException("Kalah game has been finished!");

        kalah.initKalahIndexes();
        int[] board = kalah.getBoard();

        // Validate move
        validateMove(kalah, pitId);

        if (kalah.getTurn() == null) {
            kalah.setTurn(actionSide(kalah, pitId));
        } else if (!kalah.getTurn().isOnMySide(pitId, kalah.getNoOfPits())) {
            // It is not this player turn
            throw new InvalidMoveException("It is not your turn. Wait for your opponent to move.");
        }

        // All checks passed, so move should be applied
        kalah.setState(RUNNING);
        int currentPitId = pitId;
        int stones = board[pitId];
        board[pitId] = 0; // Make this pit empty
        Kalah.PlayerTurn turn = kalah.getTurn();
        int kalahIndex = turn == PLAYER1 ? kalah.getKalah1Index() : kalah.getKalah2Index();
        while (stones > 0) {
            currentPitId = getNextPitId(kalah, currentPitId);

            // Add to stones of next pitId
            board[currentPitId]++;

            stones--;

            if (stones == 0) {
                // The last stone
                if (currentPitId != kalahIndex) {
                    if (board[currentPitId] == 1 && kalah.getTurn().isOnMySide(currentPitId, kalah.getNoOfPits())) {
                        // The pit was empty and it is last stone and is on player side
                        applyEmptyPitFilled(kalah, currentPitId, kalahIndex);
                    }

                    // Change the turn if the stone doesn't land on the player Kalah
                    kalah.setTurn(kalah.getTurn().getOpponentSide());
                }

                checkGameOver(kalah, turn);
            }
        }

        kalah.setModified(LocalDateTime.now(clock));

        return kalah;
    }

    private void checkGameOver(Kalah kalah, Kalah.PlayerTurn turn) {

        int noOfPits = kalah.getNoOfPits();
        int startIndex = turn == PLAYER1 ? 0 : noOfPits + 1;
        int[] board = kalah.getBoard();
        boolean isFinished = true;
        for (int i = startIndex; isFinished && i < startIndex + noOfPits; i++) {
            isFinished = (board[i] == 0);
        }

        if (isFinished) {
            // Set game status to finished
            kalah.setState(FINISHED);
            // Put all the remaining into opponent kalah
            startIndex = turn == PLAYER1 ? noOfPits + 1 : 0;
            int opponentKalahIndex = turn == PLAYER1 ? kalah.getKalah2Index() : kalah.getKalah1Index();

            for (int i = startIndex; i < startIndex + noOfPits; i++) {
                board[opponentKalahIndex] += board[i];
                board[i] = 0;
            }
        }
    }

    private void applyEmptyPitFilled(Kalah kalah, int pitId, int kalahIndex) {

        // Find the mirror pit of the opponent
        int diff = kalahIndex - pitId;
        int opponentPitId = pitId > kalah.getNoOfPits() ? diff - 1 : kalah.getKalah1Index() + diff;
        int[] board = kalah.getBoard();

        // Put the pit and opponent pit stones into kalah
        board[kalahIndex] += (board[pitId] + board[opponentPitId]);
        board[pitId] = 0;
        board[opponentPitId] = 0;
    }

    private int getNextPitId(Kalah kalah, int pitId) {
        pitId++;
        int[] board = kalah.getBoard();
        // rotate
        if (pitId >= board.length) pitId = 0;

        // If pitId is in the opponent kalah then jump over it
        if (!kalah.getTurn().isOnMySide(pitId, kalah.getNoOfPits()) &&
                (pitId == kalah.getKalah1Index() || pitId == kalah.getKalah2Index())) {
            pitId++;
            if (pitId >= board.length) pitId = 0;
        }

        return pitId;
    }

    private void validateMove(Kalah kalah, int pitId) throws InvalidMoveException {
        // Validate pitId
        if (pitId < 0 || pitId >= kalah.getKalah2Index())
            throw new InvalidMoveException("Pit Id should be a positive number under " + kalah.getKalah2Index());

        if (pitId == kalah.getKalah1Index() || pitId == kalah.getKalah2Index())
            throw new InvalidMoveException("Stones in kalah can not be moved.");

        // Pit should not be empty
        if (kalah.getBoard()[pitId] == 0) throw new InvalidMoveException("The pit is empty!");
    }

    /**
     * Which side tries to move its pit, if pitId is under 7 then it is PLAYER1, else it is PLAYER2
     *
     * @param kalah Kalah game
     * @param pitId Pit id
     * @return Board side that is trying to the action
     */
    private Kalah.PlayerTurn actionSide(Kalah kalah, Integer pitId) {

        return pitId < kalah.getKalah1Index() ? PLAYER1 : PLAYER2;
    }

}
