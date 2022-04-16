package com.bol.kalah.service;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 04/16/2022
 */
@Component
public class UuidIdGenerator implements IdGenerator {

    @Override
    public String generateId() {
        return UUID.randomUUID().toString();
    }
}
