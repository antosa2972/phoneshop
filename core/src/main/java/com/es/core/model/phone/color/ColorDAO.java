package com.es.core.model.phone.color;

import java.util.Optional;

public interface ColorDAO {
    Optional<Color> get(Long key);

    void save(Color color);
}
