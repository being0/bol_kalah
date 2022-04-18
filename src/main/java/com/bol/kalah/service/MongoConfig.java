package com.bol.kalah.service;

import com.mongodb.WriteConcern;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.WriteConcernResolver;

/**
 * Use write concern ACKNOWLEDGED since Optimistic Locking has been used in Kalah model.
 * See https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/#mongo-template.optimistic-locking
 *
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 04/18/2022
 */
@Configuration
public class MongoConfig {
    @Bean
    public WriteConcernResolver writeConcernResolver() {
        return action -> WriteConcern.ACKNOWLEDGED;
    }
}
