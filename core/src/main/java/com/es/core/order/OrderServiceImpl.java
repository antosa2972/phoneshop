package com.es.core.order;

import com.es.core.cart.Cart;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.order.*;
import com.es.core.model.phone.Stock;
import com.es.core.model.phone.StockDao;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderDao jdbcOrderDao;
    @Resource
    private StockDao jdbcStockDao;

    @Override
    @Transactional(rollbackFor = DataAccessException.class)
    public Long placeOrder(Cart cart, OrderDataDto orderDataDto, Long deliveryPrice) throws OutOfStockException {
        AtomicBoolean check = new AtomicBoolean(false);
        Order order = getOrder(cart, orderDataDto, deliveryPrice);
        order.getOrderItems().forEach(orderItem -> {
            Stock stock = jdbcStockDao.get(orderItem.getPhone().getId()).orElse(null);
            if (stock != null && stock.getStock() - orderItem.getQuantity() > 0) {
                jdbcStockDao.update(orderItem.getPhone().getId(), stock.getStock() - orderItem.getQuantity(),
                        stock.getReserved() - orderItem.getQuantity());
            } else {
                check.set(true);
            }
        });
        if (check.get()) {
            throw new OutOfStockException();
        }
        order.setId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
        return jdbcOrderDao.save(order);
    }

    private Order getOrder(Cart cart, OrderDataDto orderDataDto, Long deliveryPrice) {
        Order order = new Order();
        order.setSubtotal(cart.getTotalCost());
        order.setDeliveryPrice(BigDecimal.valueOf(deliveryPrice));
        order.setTotalPrice(order.getSubtotal().add(order.getDeliveryPrice()));
        order.setStatus(OrderStatus.NEW);
        order.setFirstName(orderDataDto.getFirstName());
        order.setLastName(orderDataDto.getLastName());
        order.setContactPhoneNo(orderDataDto.getPhone());
        order.setDeliveryAddress(orderDataDto.getAddress());
        order.setAdditionalInfo(orderDataDto.getAdditionalInfo());
        cart.getCartItems().forEach(cartItem -> {
            OrderItem orderItem = new OrderItem(cartItem, order);
            order.getOrderItems().add(orderItem);
        });
        return order;
    }
}
