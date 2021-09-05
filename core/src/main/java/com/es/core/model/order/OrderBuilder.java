package com.es.core.model.order;

import com.es.core.cart.Cart;

import java.math.BigDecimal;

public class OrderBuilder {
    private Order order;
    private Cart cart;
    private OrderDataDto orderDataDto;
    private Long deliveryPrice;

    public OrderBuilder(Cart cart, OrderDataDto orderDataDto, Long deliveryPrice){
        this.cart = cart;
        this.orderDataDto = orderDataDto;
        this.deliveryPrice = deliveryPrice;
        order = new Order();
    }
    public Order getOrder(){
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
