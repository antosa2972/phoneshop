package com.es.core.model.phone;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Component
public class JdbcStockDao implements StockDao {
    @Resource
    private JdbcTemplate jdbcTemplate;
    private static final String SQL_GET_STOCK = "select * from stocks where phoneId = ";
    private static final String SQL_UPDATE = "update stocks set stock = %d, reserved = %d where phoneId = %d";

    @Override
    public Optional<Stock> get(Long key) {
        List<Stock> stocks = jdbcTemplate.query(SQL_GET_STOCK + key, new BeanPropertyRowMapper<>(Stock.class));
        return stocks.stream().findFirst();
    }

    @Override
    public void update(Long key, Long stock, Long reserved) {
        String query = String.format(SQL_UPDATE, stock, reserved, key);
        jdbcTemplate.update(query);
    }
}
