package com.bol.kalah.service.mapper;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 1/10/18
 */
public interface BaseDtoDomainMapper<T, D> {

    default List<T> mapToDtoList(final List<D> domains) {

        return domains == null ? null : domains.stream().map(this::mapToDto).collect(toList());
    }

    T mapToDto(D domain);
}
