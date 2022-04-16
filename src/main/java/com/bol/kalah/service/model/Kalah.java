package com.bol.kalah.service.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Kalah game domain model
 *
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 04/16/2022
 */
@Document
@Data
@NoArgsConstructor
public class Kalah {

    @Id
    private String id;
    private GameState state;
    @CreatedDate
    private LocalDateTime created;
    @LastModifiedDate
    private LocalDateTime modified;
    /**
     * Number of stones at start, could be for example 4 or 6
     */
    private Integer noOfStones;
    /**
     * Number of pits, normally 6
     */
    private Integer noOfPits;

    private PlayerTurn turn;

    @Transient
    private int kalah1Index;
    @Transient
    private int kalah2Index;
    /**
     * Board model
     */
    private int[] board;

    public void initKalahIndexes() {
        kalah1Index = kalah1Index(noOfPits);
        kalah2Index = kalah2Index(noOfPits);
    }

    private static int kalah1Index(Integer noOfPits) {
        return noOfPits;
    }

    private static int kalah2Index(Integer noOfPits) {
        return noOfPits * 2 + 1;
    }

    public Kalah(String id, GameState state, LocalDateTime created, LocalDateTime modified,
                 Integer noOfStones, Integer noOfPits, PlayerTurn turn, int[] board) {
        this.id = id;
        this.state = state;
        this.created = created;
        this.modified = modified;
        this.noOfStones = noOfStones;
        this.noOfPits = noOfPits;
        this.turn = turn;
        this.board = board;
        // Init kalah indexes
        initKalahIndexes();
    }

    /**
     * Creates a Kalah game model
     *
     * @param id         id
     * @param noOfPits   Number of pits, normally 6
     * @param noOfStones Number of stones in each pit, exp. 4 or 6
     * @param now        current time in utc
     * @return Kalah model
     */
    public static Kalah doCreate(String id, int noOfPits, int noOfStones, LocalDateTime now) {
        if (noOfPits < 1 || noOfStones < 1) throw new IllegalArgumentException("Use positive noOfPits and noOfStones.");

        // Setup board with pits equal to numberOfStones and Kalahs with 0, no of pits will noOfPits * 2 + 2 kalahs
        int[] board = new int[noOfPits * 2 + 2];
        Arrays.fill(board, noOfStones);
        board[kalah1Index(noOfPits)] = 0;
        board[kalah2Index(noOfPits)] = 0;

        return new Kalah(id, GameState.CREATED, now, now, noOfPits, noOfStones, null, board);
    }


    /**
     * Game status could be created, running or finished
     */
    public enum GameState {
        CREATED, RUNNING, FINISHED;
    }

    /**
     * Shows whose turn is it
     */
    public enum PlayerTurn {

        PLAYER1, PLAYER2;

        public PlayerTurn getOpponentSide() {
            return this == PLAYER1 ? PLAYER2 : PLAYER1;
        }

        public boolean isOnMySide(int pitId, int noOfPits) {
            if (this == PLAYER2 && pitId > noOfPits) {
                return true;
            } else return this == PLAYER1 && pitId <= noOfPits;
        }
    }


}
