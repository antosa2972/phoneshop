package com.es.core.model.phone.color;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;

@Component
public class JdbcColorDAO implements ColorDAO {
    @Resource
    private JdbcTemplate jdbcTemplate;

    private static final String SQL_GET_COLOR = "select * from colors where id = ";
    private static final String SQL_UPDATE_COLOR = "insert into colors(id,code) values (?,?)";

    @Override
    @Transactional(readOnly = true)
    public Optional<Color> get(Long key) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_GET_COLOR + key,
                new BeanPropertyRowMapper<Color>(Color.class)));
    }

    @Override
    @Transactional(rollbackFor = DataAccessException.class)
    public void save(Color color) {
        jdbcTemplate.update(SQL_UPDATE_COLOR, color.getId(), color.getCode());
    }
}
