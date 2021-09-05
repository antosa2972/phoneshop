package com.es.core.model.order;

import com.es.core.cart.CartItem;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderResultSetExtractor implements ResultSetExtractor<Order> {
    @Resource
    private PhoneDao jdbcPhoneDao;

    @Override
    public Order extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        resultSet.next();
        List<OrderItem> orderItems = new ArrayList<>();
        Order order = new Order();
        order.setId(resultSet.getLong("orders.id"));
        order.setSubtotal(BigDecimal.valueOf(resultSet.getFloat("orders.subtotal")));
        order.setDeliveryPrice(BigDecimal.valueOf(resultSet.getFloat("orders.deliveryPrice")));
        order.setTotalPrice(BigDecimal.valueOf(resultSet.getFloat("orders.totalPrice")));
        order.setFirstName(resultSet.getString("orders.firstName"));
        order.setLastName(resultSet.getString("orders.lastName"));
        order.setDeliveryAddress(resultSet.getString("orders.deliveryAddress"));
        order.setContactPhoneNo(resultSet.getString("orders.contactPhoneNo"));
        order.setAdditionalInfo(resultSet.getString("orders.additionalInfo"));
        order.setStatus(OrderStatus.valueOf(resultSet.getString("orders.status")));
        do {
            Long orderItemId = resultSet.getLong("orderItems.id");
            Long phoneId = resultSet.getLong("orderItems.phoneId");
            Long quantity = resultSet.getLong("orderItems.quantity");
            orderItems.add(new OrderItem(orderItemId, jdbcPhoneDao.get(phoneId).orElse(null), order, quantity));
        } while ((resultSet.next()));
        order.setOrderItems(orderItems);

        return order;
    }
}
