package com.bol.kalah.service.exception;

import org.springframework.http.HttpStatus;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 04/16/2022
 */
public enum BusinessErrorsEnum {

    VALIDATION(HttpStatus.BAD_REQUEST), INVALID_MOVE(HttpStatus.CONFLICT),
    NOT_YOUR_TURN(HttpStatus.CONFLICT), CONCURRENT_UPDATE(HttpStatus.CONFLICT),
    GAME_FINISHED(HttpStatus.CONFLICT), GAME_NOT_FOUND(HttpStatus.NOT_FOUND);

    private final HttpStatus httpStatus;

    BusinessErrorsEnum(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
