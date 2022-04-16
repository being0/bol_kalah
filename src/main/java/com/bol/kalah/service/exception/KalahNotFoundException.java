package com.bol.kalah.service.exception;

import static com.bol.kalah.service.exception.BusinessErrorsEnum.GAME_NOT_FOUND;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/12/2020
 */
public class KalahNotFoundException extends BusinessException {

    public KalahNotFoundException(String message) {
        super(message, GAME_NOT_FOUND);
    }
}
