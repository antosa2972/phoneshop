package com.es.core.order;

import com.es.core.cart.Cart;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderDataDto;

public interface OrderService {
    Long placeOrder(Cart cart, OrderDataDto orderDataDto, Long deliveryPrice) throws OutOfStockException;
}
