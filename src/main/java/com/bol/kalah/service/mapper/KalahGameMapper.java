package com.bol.kalah.service.mapper;

import com.bol.kalah.service.model.Kalah;
import com.bol.kalah.to.KalahTo;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 04/16/2022
 */
@Component
public class KalahGameMapper implements BaseDtoDomainMapper<KalahTo, Kalah> {

    @Override
    public KalahTo mapToDto(Kalah kalah) {
        if (kalah == null) return null;

        // Use a linked hash map to keep the order of insertion
        LinkedHashMap<String, String> linkedMap = new LinkedHashMap<>();
        for (int i = 0; i < kalah.getBoard().length; i++)
            linkedMap.put(String.valueOf(i + 1), String.valueOf(kalah.getBoard()[i]));

        return new KalahTo(kalah.getId(), linkedMap, kalah.getTurn());
    }
}
