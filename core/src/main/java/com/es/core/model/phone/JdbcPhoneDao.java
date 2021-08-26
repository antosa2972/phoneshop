package com.es.core.model.phone;

import com.es.core.model.phone.color.Color;
import com.es.core.model.phone.color.JdbcColorDAO;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.Types;
import java.util.*;

@Component
public class JdbcPhoneDao implements PhoneDao {
    public static final String SQL_INSERT_PHONE = "insert into phones (id,brand,model,price,displaySizeInches,weightGr," +
            "lengthMm,widthMm,heightMm,announced,deviceType,os,displayResolution,pixelDensity,displayTechnology," +
            "backCameraMegapixels,frontCameraMegapixels,ramGb,internalStorageGb,batteryCapacityMah,talkTimeHours," +
            "standByTimeHours,bluetooth,positioning,imageUrl,description) values (:id,:brand,:model,:price,:displaySizeInches,:weightGr," +
            ":lengthMm,:widthMm,:heightMm,:announced,:deviceType,:os,:displayResolution,:pixelDensity," +
            ":displayTechnology,:backCameraMegapixels,:frontCameraMegapixels,:ramGb,:internalStorageGb," +
            ":batteryCapacityMah,:talkTimeHours,:standByTimeHours,:bluetooth,:positioning,:imageUrl,:description)";
    private static final String SQL_GET_ALL_PHONES = "select * from phones left join phone2color " +
            "on phones.id=phone2color.phoneId left join colors on colors.id = phone2color.colorId ";
    public static final String SQL_GET_PHONE = "select * from phones where id= ";
    private static final String SQL_SELECT_COUNT_FIND_ALL_EXTENDED = "select count(*) from phones ";
    private static final String SQL_WHERE_SEARCH = "where (phones.id in (select phoneId from stocks) and " +
            "(SELECT STOCK FROM STOCKS WHERE PHONEID = PHONES.ID AND STOCK > 0) and price is not null) ";
    public static final String SQL_SELECT_COLOR_IDS = "select colorId from phone2color where phoneId= ";

    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Resource
    private PhoneResultSetExtractor phoneResultSetExtractor;
    @Resource
    private JdbcColorDAO jdbcColorDAO;

    public Optional<Phone> get(final Long key) {
        String query = SQL_GET_PHONE + key;
        Phone phone = jdbcTemplate.queryForObject(query,new BeanPropertyRowMapper<>(Phone.class));

        String colorIdQuery = SQL_SELECT_COLOR_IDS + key;
        List<Long> colorIds = jdbcTemplate.query(colorIdQuery,
                new BeanPropertyRowMapper<>(Long.class));

        if(colorIds!=null && !colorIds.isEmpty()){
            Set<Color> colorSet = new HashSet<>();
            for(Long colorId : colorIds){
                Optional<Color> colorOptional = jdbcColorDAO.get(colorId);
                colorOptional.ifPresent(colorSet::add);
            }
            phone.setColors(colorSet);
        }
        return Optional.ofNullable(phone);
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

    @Override
    public List<Phone> findAll(String search, String sortField, String order, int offset, int limit) {
        String query = SQL_GET_ALL_PHONES + SQL_WHERE_SEARCH;
        List<Object> objects = new ArrayList<>();
        List<Integer> types = new ArrayList<>();
        if (search != null) {
            query = query + "and lower(model) like lower(?) ";
            objects.add("%" + search + "%");
            types.add(Types.VARCHAR);
        }
        if (sortField != null && order != null) {
            query = query + String.format("group by phones.id order by %s %s ",sortField,order);
        }

        //query = query + "left join phone2color on phones.id=phone2color.phoneId left join colors on colors.id = phone2color.colorId ";
        query = query + " offset " + offset + " limit " + limit;

        int[] typesArray = types.stream()
                .mapToInt(i->i)
                .toArray();
        return jdbcTemplate.query(query,objects.toArray(),typesArray,phoneResultSetExtractor);
    }

    @Override
    public Long count(final String search, final String sortField, final String order,
                      final int offset, final int limit) {
        String request = SQL_SELECT_COUNT_FIND_ALL_EXTENDED + SQL_WHERE_SEARCH;
        if (search != null) {
            request = request + "and lower(model) like lower(?)";
            return jdbcTemplate.queryForObject(request, new Object[]{"%" + search +
                    "%"}, new int[]{Types.VARCHAR}, Long.class);
        } else {
            return jdbcTemplate.queryForObject(request, Long.class);
        }
    }

}





