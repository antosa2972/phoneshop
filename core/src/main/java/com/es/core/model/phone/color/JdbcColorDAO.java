package com.es.core.model.phone.color;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.util.Optional;

public class JdbcColorDAO implements ColorDAO {
    @Resource
    private JdbcTemplate jdbcTemplate;

    private static final String SQL_GET_COLOR = "select * from colors where id = ";
    private static final String SQL_UPDATE_COLOR = "insert into colors(id,code) values (?,?)";

    @Override
    public Optional<Color> get(Long key) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_GET_COLOR + key,
                new BeanPropertyRowMapper<Color>(Color.class)));
    }

    @Override
    public void save(Color color) {
        jdbcTemplate.update(SQL_UPDATE_COLOR, color.getId(), color.getCode());
    }
}
