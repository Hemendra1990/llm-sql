//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.hemendra.llmsql.keycloak.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface BaseMapper<D, E> {
    D toDto(E var1);

    E toEntity(D var1);

    default List<D> toDtoList(List<E> entities) {
        return (List)(Objects.nonNull(entities) ? (List)entities.stream().map(this::toDto).collect(Collectors.toList()) : new ArrayList());
    }

    default List<E> toEntityList(List<D> dtoList) {
        return (List)(Objects.nonNull(dtoList) ? (List)dtoList.stream().map(this::toEntity).collect(Collectors.toList()) : new ArrayList());
    }
}
