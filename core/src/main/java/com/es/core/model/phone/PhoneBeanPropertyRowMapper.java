package com.es.core.model.phone;

import com.es.core.model.phone.color.Color;
import com.es.core.model.phone.color.JdbcColorDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class PhoneBeanPropertyRowMapper extends BeanPropertyRowMapper<Phone> {

    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private JdbcColorDAO jdbcColorDAO;

    public static final String SQL_SELECT_COLOR_IDS = "select colorId from phone2color where phoneId = ";

    public PhoneBeanPropertyRowMapper() {
        this.initialize(Phone.class);
    }

    @Override
    public Phone mapRow(ResultSet rs, int rowNumber) throws SQLException {
        Phone phone = super.mapRow(rs, rowNumber);
        List<Long> colorsIds = jdbcTemplate.query(SQL_SELECT_COLOR_IDS + phone.getId(), new IdRowMapper());
        if (!colorsIds.isEmpty()) {
            Set<Color> colorSet = new HashSet<>();
            for (Long colorId : colorsIds) {
                Optional<Color> optionalColor = jdbcColorDAO.get(colorId);
                optionalColor.ifPresent(colorSet::add);
            }
            phone.setColors(colorSet);
        }
        return phone;
    }

    private final static class IdRowMapper implements RowMapper<Long> {

        @Override
        public Long mapRow(ResultSet resultSet, int i) throws SQLException {
            return resultSet.getLong("colorId");
        }
    }
}
