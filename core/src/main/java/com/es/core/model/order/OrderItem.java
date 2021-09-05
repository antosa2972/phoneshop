package com.es.core.model.order;

import com.es.core.cart.CartItem;
import com.es.core.model.phone.Phone;

public class OrderItem {
    private Long id;
    private Phone phone;
    private Order order;
    private Long quantity;

    public OrderItem(Long id, Phone phone, Order order, Long quantity) {
        this.id = id;
        this.phone = phone;
        this.order = order;
        this.quantity = quantity;
    }

    public OrderItem(CartItem cartItem, Order order) {
        this.phone = cartItem.getPhone();
        this.quantity = cartItem.getQuantity();
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(final Phone phone) {
        this.phone = phone;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(final Order order) {
        this.order = order;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(final Long quantity) {
        this.quantity = quantity;
    }
}
