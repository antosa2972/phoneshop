package com.es.core.model.phone;

import java.util.Optional;

public interface StockDao {
    Optional<Stock> get(Long key);

    void update(Long key, Long stock, Long reserved);
}
