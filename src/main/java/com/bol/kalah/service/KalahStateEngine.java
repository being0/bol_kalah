package com.bol.kalah.service;

import com.bol.kalah.service.exception.InvalidMoveException;
import com.bol.kalah.service.exception.KalahFinishedException;
import com.bol.kalah.service.model.Kalah;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 04/16/2022
 */
public interface KalahStateEngine {

    /**
     * Move kalah to new state based on the selected pitId
     *
     * @param kalah kalah game
     * @param pitId pit id
     * @return kalah game with updated state
     */
    Kalah move(Kalah kalah, int pitId) throws KalahFinishedException, InvalidMoveException;
}
