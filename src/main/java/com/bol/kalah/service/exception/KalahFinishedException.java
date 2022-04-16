package com.bol.kalah.service.exception;

import static com.bol.kalah.service.exception.BusinessErrorsEnum.GAME_FINISHED;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 04/16/2022
 */
public class KalahFinishedException extends BusinessException {

    public KalahFinishedException(String message) {
        super(message, GAME_FINISHED);
    }
}
