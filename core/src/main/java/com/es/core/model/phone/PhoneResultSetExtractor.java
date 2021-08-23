package com.es.core.model.phone;

import com.es.core.model.phone.color.Color;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class PhoneResultSetExtractor implements ResultSetExtractor<List<Phone>> {
    @Override
    public List<Phone> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        Map<Phone, Set<Color>> data = new LinkedHashMap<>();
        while (resultSet.next()){
            Phone phone = new Phone();
            phone.setId(resultSet.getLong("id"));
            phone.setBrand(resultSet.getString("brand"));
            phone.setModel(resultSet.getString("model"));
            phone.setPrice(BigDecimal.valueOf(resultSet.getLong("price")));
            phone.setDisplaySizeInches(BigDecimal.valueOf(resultSet.getLong("displaySizeInches")));
            phone.setWeightGr(resultSet.getInt("weightGr"));
            phone.setWidthMm(BigDecimal.valueOf(resultSet.getFloat("lengthMm")));
            phone.setHeightMm(BigDecimal.valueOf(resultSet.getFloat("heightMm")));
            phone.setAnnounced(resultSet.getDate("announced"));
            phone.setDeviceType(resultSet.getString("deviceType"));
            phone.setOs(resultSet.getString("os"));
            phone.setDisplayResolution(resultSet.getString("displayResolution"));
            phone.setPixelDensity(resultSet.getInt("pixelDensity"));
            phone.setDisplayTechnology(resultSet.getString("displayTechnology"));
            phone.setBackCameraMegapixels(BigDecimal.valueOf(resultSet.getFloat("backCameraMegapixels")));
            phone.setFrontCameraMegapixels(BigDecimal.valueOf(resultSet.getFloat("frontCameraMegapixels")));
            phone.setRamGb(BigDecimal.valueOf(resultSet.getFloat("ramGb")));
            phone.setInternalStorageGb(BigDecimal.valueOf(resultSet.getFloat("internalStorageGb")));
            phone.setBatteryCapacityMah(resultSet.getInt("batteryCapacityMah"));
            phone.setTalkTimeHours(BigDecimal.valueOf(resultSet.getFloat("talkTimeHours")));
            phone.setStandByTimeHours(BigDecimal.valueOf(resultSet.getFloat("standByTimeHours")));
            phone.setBluetooth(resultSet.getString("bluetooth"));
            phone.setPositioning(resultSet.getString("positioning"));
            phone.setPositioning(resultSet.getString("positioning"));
            phone.setImageUrl(resultSet.getString("imageUrl"));
            phone.setDescription(resultSet.getString("description"));

            data.putIfAbsent(phone,new HashSet<>());
            Color color = new Color();
            color.setId(resultSet.getLong("colors.id"));
            color.setCode(resultSet.getString("code"));
            data.get(phone).add(color);
        }
        List<Phone> phoneList = new ArrayList<>();
        for(Map.Entry<Phone,Set<Color>> entry : data.entrySet()){
            Phone phoneToAdd = entry.getKey();
            phoneToAdd.setColors(entry.getValue());
            phoneList.add(phoneToAdd);
        }
        return phoneList;
    }
}
