package com.bol.kalah.service.repository;

import com.bol.kalah.service.model.Kalah;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 04/16/2022
 */
public interface KalahRepository extends MongoRepository<Kalah, String> {
}
