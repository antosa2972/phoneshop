package com.es.core.model.phone;

import com.es.core.model.ParamsForSearch;

import java.util.List;
import java.util.Optional;

public interface PhoneDao {
    Optional<Phone> get(Long key);

    void save(Phone phone);

    List<Phone> findAll(int offset, int limit);

    List<Phone> findAll(ParamsForSearch paramsForSearch);
    Long count(ParamsForSearch paramsForSearch);
}
