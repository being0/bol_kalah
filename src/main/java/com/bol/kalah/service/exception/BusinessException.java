package com.bol.kalah.service.exception;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 04/16/2022
 */
public abstract class BusinessException extends RuntimeException {

    private final BusinessErrorsEnum businessErrorsEnum;

    protected BusinessException(String message, BusinessErrorsEnum businessErrorsEnum) {
        super(message);
        this.businessErrorsEnum = businessErrorsEnum;
    }

    public BusinessErrorsEnum getBusinessErrorsEnum() {
        return businessErrorsEnum;
    }
}
