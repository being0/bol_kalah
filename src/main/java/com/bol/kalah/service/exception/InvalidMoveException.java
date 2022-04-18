package com.bol.kalah.service.exception;

import static com.bol.kalah.service.exception.BusinessErrorsEnum.INVALID_MOVE;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 04/16/2022
 */
public class InvalidMoveException extends BusinessException{

    public InvalidMoveException(String message) {
        super(message, INVALID_MOVE);
    }

    public InvalidMoveException(String message, BusinessErrorsEnum businessErrorsEnum) {
        super(message, businessErrorsEnum);
    }
}
