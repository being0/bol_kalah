package com.bol.kalah.controller;

import com.bol.kalah.service.KalahService;
import com.bol.kalah.service.exception.InvalidMoveException;
import com.bol.kalah.service.exception.KalahFinishedException;
import com.bol.kalah.service.exception.KalahNotFoundException;
import com.bol.kalah.to.KalahTo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller provides post api on /games and put api to play with the Kalah and move the specified pit
 *
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 04/16/2022
 */
@RestController
@RequestMapping(value = "/games")
@RequiredArgsConstructor
public class KalahController {

    private final KalahService kalahService;

    /**
     * Creates a new game
     *
     * @return Kalah game
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public KalahTo create() {

        return kalahService.create();
    }

    /**
     * Moves game to the next step.
     *
     * @param gameId game id
     * @param pitId  pit id starting from 1
     * @return Kalah game
     */
    @PutMapping("/{gameId}/pits/{pitId}")
    public KalahTo move(@PathVariable("gameId") String gameId, @PathVariable("pitId") Integer pitId)
            throws KalahNotFoundException, InvalidMoveException, KalahFinishedException {

        return kalahService.move(gameId, pitId);
    }

    /**
     * Gets a Kalah game by id
     *
     * @param gameId game id
     * @return Kalah game
     */
    @GetMapping("/{gameId}")
    public KalahTo get(@PathVariable("gameId") String gameId) {

        return kalahService.get(gameId);
    }

}
