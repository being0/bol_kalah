package com.bol.kalah.service;

import com.bol.kalah.service.exception.InvalidMoveException;
import com.bol.kalah.service.exception.KalahFinishedException;
import com.bol.kalah.service.exception.KalahNotFoundException;
import com.bol.kalah.to.KalahTo;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 04/16/2022
 */
@Validated
public interface KalahService {

    /**
     * Create a Kalah game
     *
     * @return Kalah game
     */
    KalahTo create();

    /**
     * Moves the Kalah game to next state according to the selected pit id
     *
     * @param gameId game id
     * @param pitId  pit id starting from 1
     * @return Kalah game
     */
    KalahTo move(@NotNull @Size(min = 1, max = 40) String gameId, @NotNull @Min(1) @Max(13) Integer pitId)
            throws KalahNotFoundException, InvalidMoveException, KalahFinishedException;


    /**
     * Gets a Kalah game by id
     *
     * @param gameId game id
     * @return Kalah game
     */
    KalahTo get(@NotNull @Size(min = 1, max = 40) String gameId) throws KalahNotFoundException;

}
