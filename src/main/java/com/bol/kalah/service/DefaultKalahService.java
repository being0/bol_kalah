package com.bol.kalah.service;

import com.bol.kalah.service.exception.InvalidMoveException;
import com.bol.kalah.service.exception.KalahFinishedException;
import com.bol.kalah.service.exception.KalahNotFoundException;
import com.bol.kalah.service.lock.LockProvider;
import com.bol.kalah.service.mapper.KalahGameMapper;
import com.bol.kalah.service.model.Kalah;
import com.bol.kalah.service.repository.KalahRepository;
import com.bol.kalah.to.KalahTo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 04/16/2022
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class DefaultKalahService implements KalahService {

    private final KalahGameMapper kalahGameMapper;
    private final KalahRepository kalahRepository;
    private final LockProvider lockProvider;
    private final IdGenerator idGenerator;
    private final KalahStateEngine kalahStateEngine;
    private final Clock clock;

    @Value("${my.kalah.noOfPits:6}")
    private int noOfPits;
    @Value("${my.kalah.noOfStones:6}")
    private int noOfStones;

    @Override
    public KalahTo create() {

        // Create a new kalah game
        String id = idGenerator.generateId();
        Kalah kalah = Kalah.doCreate(id, noOfPits, noOfStones, LocalDateTime.now(clock));

        // Create kalah game on repository(we can raise async message here after creation for other parities interest, however for simplicity of this project async message is ignored)
        Kalah createdKalah = kalahRepository.save(kalah);

        log.info("Kalah by id {} created!", id);

        return kalahGameMapper.mapToDto(createdKalah);
    }

    @Override
    public KalahTo move(String gameId, Integer pitId) throws KalahNotFoundException, InvalidMoveException, KalahFinishedException {

        // Find the game by id
        Kalah kalah = loadKalah(gameId);

        // Do the movement in a distributed lock, concurrency on the same game can produce inconsistent states
        Kalah movedGame = lockProvider.doInLock(kalah, (kg) -> {
            // Move the Kalah game
            Kalah movedKalah = kalahStateEngine.move(kalah, pitId);
            // Save on repository
            return kalahRepository.save(movedKalah);
        });

        log.debug("Kalah {} moved for pitId {} final kalah is {}", gameId, pitId, kalah);

        return kalahGameMapper.mapToDto(movedGame);
    }

    @Override
    public KalahTo get(String gameId) {

        return kalahGameMapper.mapToDto(loadKalah(gameId));
    }

    private Kalah loadKalah(String gameId) {

        return kalahRepository.findById(gameId)
                .orElseThrow(() -> new KalahNotFoundException("No Kalah game found for the specified id!"));
    }

}
