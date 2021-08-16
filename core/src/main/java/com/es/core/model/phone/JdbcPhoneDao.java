package com.es.core.model.phone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class JdbcPhoneDao implements PhoneDao {
    public static final String SQL_INSERT_PHONE = "insert into phones (id, brand, model, price,displaySizeInches, weightGr," +
            " lengthMm, widthMm, heightMm, announced, deviceType, os, displayResolution," +
            "pixelDensity, displayTechnology, backCameraMegapixels, frontCameraMegapixels, ramGb, internalStorageGb," +
            "batteryCapacityMah, talkTimeHours, standByTimeHours, bluetooth, positioning, imageUrl, description) " +
            "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String SQL_GET_ALL_PHONES = "select * from phones offset ";
    public static final String SQL_GET_PHONE = "select * from phones where id= ";

    @Resource
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private PhoneBeanPropertyRowMapper phoneBeanPropertyRowMapper;

    public Optional<Phone> get(final Long key) {
        String query = SQL_GET_PHONE + key;
        return Optional.ofNullable(jdbcTemplate.queryForObject(query, phoneBeanPropertyRowMapper));
    }

    public void save(final Phone phone) {
        Long phoneId = phone.getId();
        String phoneBrand = phone.getBrand();
        String phoneModel = phone.getModel();
        BigDecimal phonePrice = phone.getPrice();
        BigDecimal displaySizeInches = phone.getDisplaySizeInches();
        Integer weightGr = phone.getWeightGr();
        BigDecimal lengthMm = phone.getLengthMm();
        BigDecimal widthMm = phone.getWidthMm();
        BigDecimal heightMm = phone.getHeightMm();
        Date announced = phone.getAnnounced();
        String deviceType = phone.getDeviceType();
        String OS = phone.getOs();
        String displayResolution = phone.getDisplayResolution();
        Integer pixelDensity = phone.getPixelDensity();
        String displayTechnology = phone.getDisplayTechnology();
        BigDecimal backCameraMegapixels = phone.getBackCameraMegapixels();
        BigDecimal frontCameraMegapixels = phone.getFrontCameraMegapixels();
        BigDecimal ramGb = phone.getRamGb();
        BigDecimal internalStorageGb = phone.getInternalStorageGb();
        Integer batteryCapacityMah = phone.getBatteryCapacityMah();
        BigDecimal talkTimeHours = phone.getTalkTimeHours();
        BigDecimal standByTimeHours = phone.getStandByTimeHours();
        String bluetooth = phone.getBluetooth();
        String positioning = phone.getPositioning();
        String imageUrl = phone.getImageUrl();
        String description = phone.getDescription();
        jdbcTemplate.update(SQL_INSERT_PHONE, phoneId, phoneBrand, phoneModel, phonePrice, displaySizeInches, weightGr,
                lengthMm, widthMm, heightMm, announced, deviceType, OS, displayResolution, pixelDensity,
                displayTechnology, backCameraMegapixels, frontCameraMegapixels, ramGb, internalStorageGb,
                batteryCapacityMah, talkTimeHours, standByTimeHours, bluetooth, positioning, imageUrl, description);
    }

    public List<Phone> findAll(int offset, int limit) {
        String query = SQL_GET_ALL_PHONES + offset + " limit " + limit;
        return jdbcTemplate.query(query, phoneBeanPropertyRowMapper);
    }

}





