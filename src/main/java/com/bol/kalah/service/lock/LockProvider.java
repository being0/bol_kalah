package com.bol.kalah.service.lock;

import com.bol.kalah.service.exception.InvalidMoveException;
import com.bol.kalah.service.exception.KalahFinishedException;
import com.bol.kalah.service.model.Kalah;

import java.util.function.Function;

/**
 * Provides lock and doInLock does the function in a locked and synchronized block
 *
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/12/2020
 */
public interface LockProvider {

    /**
     * Do the function in lock
     *
     * @param kalahGame kalah game
     * @param kalahGameFunc function that should be done in lock
     * @return kalah game
     */
    Kalah doInLock(Kalah kalahGame, Function<Kalah, Kalah> kalahGameFunc);
}
