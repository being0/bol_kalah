package com.bol.kalah.service.lock;

import com.bol.kalah.service.model.Kalah;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.locks.Lock;
import java.util.function.Function;

/**
 * Provides distributed lock using Redis and Redisson client
 *
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/12/2020
 */
@Component
public class RedissionLockProvider implements LockProvider {

    @Resource
    private RedissonClient redissonClient;

    /**
     * Do the function in a distributed lock base on game id, the lock is provided by Redis using Redission client.
     * Using fair lock preserve the order of calls
     *
     * @param kalahGame     kalah game
     * @param kalahGameFunc function that should be done in lock
     * @return Kalah game
     */
    @Override
    public Kalah doInLock(Kalah kalahGame, Function<Kalah, Kalah> kalahGameFunc) {

        // Do the action in fair lock so the order is preserved
        Lock lock = redissonClient.getFairLock("kalah_game_" + kalahGame.getId());
        lock.lock();
        try {
            return kalahGameFunc.apply(kalahGame);
        } finally {
            lock.unlock();
        }
    }
}
