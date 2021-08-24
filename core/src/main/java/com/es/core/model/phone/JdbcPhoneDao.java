package com.es.core.model.phone;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Component
public class JdbcPhoneDao implements PhoneDao {
    public static final String SQL_INSERT_PHONE = "insert into phones (id,brand,model,price,displaySizeInches,weightGr," +
            "lengthMm,widthMm,heightMm,announced,deviceType,os,displayResolution,pixelDensity,displayTechnology," +
            "backCameraMegapixels,frontCameraMegapixels,ramGb,internalStorageGb,batteryCapacityMah,talkTimeHours," +
            "standByTimeHours,bluetooth,positioning,imageUrl,description) values (:id,:brand,:model,:price,:displaySizeInches,:weightGr," +
            ":lengthMm,:widthMm,:heightMm,:announced,:deviceType,:os,:displayResolution,:pixelDensity," +
            ":displayTechnology,:backCameraMegapixels,:frontCameraMegapixels,:ramGb,:internalStorageGb," +
            ":batteryCapacityMah,:talkTimeHours,:standByTimeHours,:bluetooth,:positioning,:imageUrl,:description)";
    private static final String SQL_GET_ALL_PHONES = "select * from phones left join phone2color on phones.id=phone2color.phoneId" +
            " join colors on colors.id = phone2color.colorId ";
    public static final String SQL_GET_PHONE = "select * from phones where id= ";

    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Resource
    private PhoneResultSetExtractor phoneResultSetExtractor;

    public Optional<Phone> get(final Long key) {
        String query = SQL_GET_PHONE + key;
        return Optional.ofNullable(jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>()));
    }

    public void save(final Phone phone) {
        MapSqlParameterSource in = new MapSqlParameterSource();
        in.addValue("id", phone.getId());
        in.addValue("brand", phone.getBrand());
        in.addValue("model", phone.getModel());
        in.addValue("price", phone.getPrice());
        in.addValue("displaySizeInches", phone.getDisplaySizeInches());
        in.addValue("weightGr", phone.getWeightGr());
        in.addValue("lengthMm", phone.getLengthMm());
        in.addValue("widthMm", phone.getWidthMm());
        in.addValue("heightMm", phone.getHeightMm());
        in.addValue("announced", phone.getAnnounced());
        in.addValue("deviceType", phone.getDeviceType());
        in.addValue("os", phone.getOs());
        in.addValue("displayResolution", phone.getDisplayResolution());
        in.addValue("pixelDensity", phone.getPixelDensity());
        in.addValue("displayTechnology", phone.getDisplayTechnology());
        in.addValue("backCameraMegapixels", phone.getBackCameraMegapixels());
        in.addValue("frontCameraMegapixels", phone.getFrontCameraMegapixels());
        in.addValue("ramGb", phone.getRamGb());
        in.addValue("internalStorageGb", phone.getInternalStorageGb());
        in.addValue("batteryCapacityMah", phone.getBatteryCapacityMah());
        in.addValue("talkTimeHours", phone.getTalkTimeHours());
        in.addValue("standByTimeHours", phone.getStandByTimeHours());
        in.addValue("bluetooth", phone.getBluetooth());
        in.addValue("positioning", phone.getPositioning());
        in.addValue("imageUrl", phone.getImageUrl());
        in.addValue("description", phone.getDescription());
        namedParameterJdbcTemplate.update(SQL_INSERT_PHONE, in);
    }

    public List<Phone> findAll(int offset, int limit) {
        String query = SQL_GET_ALL_PHONES + " offset " + offset + " limit " + limit;
        return jdbcTemplate.query(query, phoneResultSetExtractor);
    }

}





