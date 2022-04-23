package com.bol.kalah.service;

import com.bol.kalah.service.exception.InvalidMoveException;
import com.bol.kalah.service.exception.KalahFinishedException;
import com.bol.kalah.service.exception.NotYourTurnException;
import com.bol.kalah.service.exception.ValidationException;
import com.bol.kalah.service.model.Kalah;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

import static com.bol.kalah.service.model.Kalah.GameState.FINISHED;
import static com.bol.kalah.service.model.Kalah.GameState.RUNNING;
import static com.bol.kalah.service.model.Kalah.PlayerTurn.PLAYER1;
import static com.bol.kalah.service.model.Kalah.PlayerTurn.PLAYER2;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 04/16/2022
 */
@Component
@RequiredArgsConstructor
public class TwoPlayersKalahStateEngine implements KalahStateEngine {

    private final Clock clock;

    /**
     * Moves stones from the specified pit id.
     *
     * @param pitId pit id starting from 1
     * @return Kalah game
     */
    @Override
    public Kalah move(Kalah kalah, int pitId) throws KalahFinishedException, InvalidMoveException {

        // Create execution context
        int pitIndex = pitId - 1;
        Context context = Context.create(pitIndex, kalah.getNoOfPits());
        if (kalah.getTurn() == null) kalah.setTurn(context.turn);

        // 1- Validate move
        validateMove(kalah, pitIndex, context);

        // 2- Distribute stones(RUNNING)
        int currentPitIdx = distributeStones(kalah, pitIndex, context);

        // 3- Apply last stone rule
        applyLastStoneRule(kalah, currentPitIdx, context);

        // 4- Check game over
        checkGameOver(kalah, context);

        return kalah;
    }

    private int distributeStones(Kalah kalah, int pitIndex, Context context) {
        kalah.setState(RUNNING);
        int[] board = kalah.getBoard();
        int currentPitIdx = pitIndex;
        int stones = board[pitIndex];
        board[pitIndex] = 0; // Make this pit empty

        // Put stones in pits
        for (; stones > 0; stones--) {
            currentPitIdx = getNextPitIdx(kalah, currentPitIdx, context);
            // Add to the stones of next pitIdx
            board[currentPitIdx]++;
        }

        kalah.setModified(LocalDateTime.now(clock));

        return currentPitIdx;
    }

    private void applyLastStoneRule(Kalah kalah, int currentPitIdx, Context context) {
        // The last stone
        if (currentPitIdx != context.kalahIndex) {
            if (kalah.getBoard()[currentPitIdx] == 1 && kalah.getTurn().isOnMySide(currentPitIdx, kalah.getNoOfPits())) {
                // The pit was empty and it is last stone and is on player side
                applyEmptyPitFilled(kalah, currentPitIdx, context);
            }

            // Change the turn if the stone doesn't land on the player Kalah
            kalah.setTurn(kalah.getTurn().getOpponentSide());
        }
    }

    private void checkGameOver(Kalah kalah, Context context) {

        int noOfPits = kalah.getNoOfPits();
        int[] board = kalah.getBoard();
        boolean isFinished = true;
        for (int i = context.startIndex; isFinished && i < context.startIndex + noOfPits; i++) {
            isFinished = (board[i] == 0);
        }

        if (isFinished) {
            // Set game status to finished
            kalah.setState(FINISHED);

            for (int i = context.opponent.startIndex; i < context.opponent.startIndex + noOfPits; i++) {
                board[context.opponent.kalahIndex] += board[i];
                board[i] = 0;
            }
        }
    }

    private void applyEmptyPitFilled(Kalah kalah, int pitIdx, Context context) {

        // Find the mirror pit of the opponent
        int diff = context.kalahIndex - pitIdx;
        int opponentPitIdx = pitIdx > kalah.getNoOfPits() ? diff - 1 : context.kalahIndex + diff;
        int[] board = kalah.getBoard();

        // Put the pit and opponent pit stones into kalah
        board[context.kalahIndex] += (board[pitIdx] + board[opponentPitIdx]);
        board[pitIdx] = 0;
        board[opponentPitIdx] = 0;
    }

    private int getNextPitIdx(Kalah kalah, int pitIdx, Context context) {
        pitIdx++;
        int[] board = kalah.getBoard();
        // rotate
        if (pitIdx >= board.length) pitIdx = 0;

        // If pitIdx is in the opponent kalah then jump over it
        if (!kalah.getTurn().isOnMySide(pitIdx, kalah.getNoOfPits()) &&
                (pitIdx == context.opponent.kalahIndex)) {
            pitIdx++;
            if (pitIdx >= board.length) pitIdx = 0;
        }

        return pitIdx;
    }

    private void validateMove(Kalah kalah, int pitIdx, Context context) throws InvalidMoveException {
        // If game is over then throw exception
        if (kalah.getState() == FINISHED) throw new KalahFinishedException("Kalah game has been finished!");

        // Validate pitIdx
        if (pitIdx < 0 || pitIdx > 2 * kalah.getNoOfPits() + 1)
            throw new ValidationException("Pit Id should be a positive number under " + kalah.getNoOfPits() + 2 * kalah.getNoOfPits() + 1);

        if (pitIdx == context.kalahIndex)
            throw new InvalidMoveException("Stones in kalah can not be moved.");

        // Pit should not be empty
        if (kalah.getBoard()[pitIdx] == 0) throw new InvalidMoveException("The pit is empty!");

        if (kalah.getTurn() != null && !kalah.getTurn().isOnMySide(pitIdx, kalah.getNoOfPits())) {
            // It is not this player turn
            throw new NotYourTurnException("It is not your turn. Wait for your opponent to move.");
        }
    }

    @AllArgsConstructor
    private static class Context {
        Kalah.PlayerTurn turn;
        Integer kalahIndex;
        Integer startIndex;
        private Context opponent;

        private static Context create(int pitIdx, int noOfPits) {
            if (pitIdx < noOfPits) {
                // Player1 context
                return new Context(PLAYER1, Kalah.calKalah1Index(noOfPits), 0,
                        new Context(PLAYER2, Kalah.calKalah2Index(noOfPits), noOfPits + 1, null));
            } else {
                // Player2 context
                return new Context(PLAYER2, Kalah.calKalah2Index(noOfPits), noOfPits + 1,
                        new Context(PLAYER1, Kalah.calKalah1Index(noOfPits), 0, null));
            }
        }
    }

}
