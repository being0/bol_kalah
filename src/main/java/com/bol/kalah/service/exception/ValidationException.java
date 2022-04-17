package com.bol.kalah.service.exception;

import static com.bol.kalah.service.exception.BusinessErrorsEnum.VALIDATION;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 04/17/2022
 */
public class ValidationException extends BusinessException {

    public ValidationException(String message) {
        super(message, VALIDATION);
    }
}
