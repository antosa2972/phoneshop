package com.es.core.model.phone;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

public interface PhoneDao {
    Optional<Phone> get(Long key);

    void save(Phone phone);

    List<Phone> findAll(int offset, int limit);

    List<Phone> findAll(final String search, final String sortField, final String order,
               final int offset, final int limit);
    Long count(final String search, final String sortField, final String order,
               final int offset, final int limit);
}
