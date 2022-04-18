package com.bol.kalah.service.exception;

import static com.bol.kalah.service.exception.BusinessErrorsEnum.NOT_YOUR_TURN;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 04/18/2022
 */
public class NotYourTurnException extends InvalidMoveException {

    public NotYourTurnException(String message) {
        super(message, NOT_YOUR_TURN);
    }
}
