package com.es.core.filter;

import com.es.core.sortenums.SortField;
import org.springframework.stereotype.Service;

@Service
public class FilterServiceImpl implements FilterService {
    @Override
    public String checkFieldVal(String field) {
        if (field != null) {
            try {
                SortField sortField = SortField.valueOf(field);
                return sortField.name();
            }catch (IllegalArgumentException e){
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public String checkOrderVal(String order) {
        if (order != null) {
            try {
                SortField sortField = SortField.valueOf(order);
                return sortField.name();
            }catch (IllegalArgumentException e){
                return null;
            }
        } else {
            return null;
        }
    }
}
