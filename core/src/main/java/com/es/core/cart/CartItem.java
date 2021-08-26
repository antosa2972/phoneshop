package com.es.core.cart;

import com.es.core.model.phone.Phone;

import java.math.BigDecimal;

public class CartItem {
    private Phone phone;
    private Long quantity;
    private BigDecimal price;

    public CartItem(Phone phone, Long quantity,BigDecimal price) {
        this.phone = phone;
        this.quantity = quantity;
        this.price = price;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
