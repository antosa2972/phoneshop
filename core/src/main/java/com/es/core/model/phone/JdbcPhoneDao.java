package com.es.core.model.phone;

import com.es.core.model.phone.color.Color;
import com.es.core.model.phone.color.JdbcColorDAO;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class JdbcPhoneDao implements PhoneDao {
    public static final String SQL_INSERT_PHONE = "insert into phones (id, brand, model, price,displaySizeInches, weightGr," +
            " lengthMm, widthMm, heightMm, announced, deviceType, os, displayResolution," +
            "pixelDensity, displayTechnology, backCameraMegapixels, frontCameraMegapixels, ramGb, internalStorageGb," +
            "batteryCapacityMah, talkTimeHours, standByTimeHours, bluetooth, positioning, imageUrl, description) " +
            "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String SQL_GET_ALL_PHONES = "select * from phones offset ";
    public static final String SQL_GET_PHONE = "select * from phones where id= ";
    public static final String SQL_SELECT_COLOR_IDS = "select colorId from phone2color where phoneId = ";
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private JdbcColorDAO jdbcColorDAO;


    public Optional<Phone> get(final Long key) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_GET_PHONE + key, new BeanPropertyRowMapper<>(Phone.class)));
    }

    public void save(final Phone phone) {
        jdbcTemplate.update(SQL_INSERT_PHONE, phone.getId(), phone.getBrand(), phone.getModel(),
                phone.getPrice(), phone.getDisplaySizeInches(), phone.getWeightGr(), phone.getLengthMm(),
                phone.getWidthMm(), phone.getHeightMm(),
                phone.getAnnounced(), phone.getDeviceType(), phone.getOs(), phone.getDisplayResolution(),
                phone.getPixelDensity(),
                phone.getDisplayTechnology(), phone.getBackCameraMegapixels(), phone.getFrontCameraMegapixels(),
                phone.getRamGb(), phone.getInternalStorageGb(), phone.getBatteryCapacityMah(), phone.getTalkTimeHours(),
                phone.getStandByTimeHours(), phone.getBluetooth(), phone.getPositioning(), phone.getImageUrl(),
                phone.getDescription());
    }

    public List<Phone> findAll(int offset, int limit) {
        return jdbcTemplate.query(SQL_GET_ALL_PHONES + offset + " limit " + limit, new BeanPropertyRowMapper(Phone.class));
    }


    private class PhoneBeanPropertyRowMapper extends BeanPropertyRowMapper<Phone> {

        public PhoneBeanPropertyRowMapper() {
            this.initialize(Phone.class);
        }

        @Override
        public Phone mapRow(ResultSet rs, int rowNumber) throws SQLException {
            Phone phone = super.mapRow(rs, rowNumber);
            List<Long> colorsIds = jdbcTemplate.query(SQL_SELECT_COLOR_IDS + phone.getId(), new BeanPropertyRowMapper<>());
            if (!colorsIds.isEmpty()) {
                Set<Color> colorSet = new HashSet<>();
                for (Long colorId : colorsIds) {
                    Optional<Color> optionalColor = jdbcColorDAO.get(colorId);
                    if (optionalColor.isPresent()) {
                        colorSet.add(optionalColor.get());
                    }
                }
                phone.setColors(colorSet);
            }
            return phone;
        }
    }
}


