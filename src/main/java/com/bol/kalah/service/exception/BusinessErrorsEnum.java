package com.bol.kalah.service.exception;

import org.springframework.http.HttpStatus;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 04/16/2022
 */
public enum BusinessErrorsEnum {

    INVALID_MOVE(HttpStatus.BAD_REQUEST), GAME_FINISHED(HttpStatus.CONFLICT), GAME_NOT_FOUND(HttpStatus.NOT_FOUND);

    private final HttpStatus httpStatus;

    BusinessErrorsEnum(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
