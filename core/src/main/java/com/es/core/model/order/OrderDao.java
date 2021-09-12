package com.es.core.model.order;

import java.util.List;
import java.util.Optional;

public interface OrderDao {

    Optional<Order> get(final Long key);

    Long save(final Order order);

    List<Order> getOrders(int limit, int offset);

    void updateStatus(OrderStatus orderStatus, Long key);
}
