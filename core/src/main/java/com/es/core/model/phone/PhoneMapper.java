package com.es.core.model.phone;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PhoneMapper implements RowMapper<Phone> {
    @Override
    public Phone mapRow(ResultSet resultSet, int i) throws SQLException {
        return null;
    }
}
