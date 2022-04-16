package com.bol.kalah.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Data transfer object for kalah game
 *
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/12/2020
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KalahTo {

    private String id;

    /**
     * the status of game for example {"1":"4","2":"4","3":"4","4":"4","5":"4","6":"4","7":"0","8":"4","
     * 9":"4","10":"4","11":"4","12":"4","13":"4","14":"0"}
     */
    private Map<String, String> status;

}
