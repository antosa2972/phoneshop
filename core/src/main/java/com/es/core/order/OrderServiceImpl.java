package com.es.core.order;

import com.es.core.cart.Cart;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderBuilder;
import com.es.core.model.order.OrderDao;
import com.es.core.model.order.OrderDataDto;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderDao jdbcOrderDao;

    @Override
    public Long placeOrder(Cart cart, OrderDataDto orderDataDto, Long deliveryPrice) throws OutOfStockException {
        OrderBuilder orderBuilder = new OrderBuilder(cart,orderDataDto,deliveryPrice);
        Order order = orderBuilder.getOrder();
        order.setId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
        return jdbcOrderDao.save(order);
    }
}
