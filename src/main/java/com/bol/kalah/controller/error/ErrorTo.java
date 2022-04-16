package com.bol.kalah.controller.error;

import com.bol.kalah.service.exception.BusinessErrorsEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 04/16/2022
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorTo {

    private String message;
    private BusinessErrorsEnum errorCode;

    public ErrorTo(String message) {
        this.message = message;
    }
}
