package com.bol.kalah.service.lock;

import com.bol.kalah.service.model.Kalah;

import java.util.function.UnaryOperator;

/**
 * Provides lock and doInLock does the function in a locked and synchronized block
 *
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 04/16/2022
 */
public interface LockProvider {

    /**
     * Do the function in lock
     *
     * @param kalahGame kalah game
     * @param kalahGameFunc function that should be done in lock
     * @return kalah game
     */
    Kalah doInLock(Kalah kalahGame, UnaryOperator<Kalah> kalahGameFunc);
}
