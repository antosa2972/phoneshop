package com.es.core.model.order;

import com.es.core.model.phone.PhoneDao;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderListResultSetExtractor implements ResultSetExtractor<List<Order>> {
    @Resource
    private PhoneDao jdbcPhoneDao;

    @Override
    public List<Order> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        Map<Order, List<OrderItem>> data = new LinkedHashMap<>();
        List<Order> orders = new ArrayList<>();
        while (resultSet.next()) {

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
            order.setDate(resultSet.getObject("orders.date", Timestamp.class));
            Long orderItemId = resultSet.getLong("orderItems.id");
            Long phoneId = resultSet.getLong("orderItems.phoneId");
            Long quantity = resultSet.getLong("orderItems.quantity");

            data.putIfAbsent(order, new ArrayList<>());
            OrderItem orderItem = new OrderItem(orderItemId, jdbcPhoneDao.get(phoneId).orElse(null), order, quantity);
            data.get(order).add(orderItem);
        }
        for (Map.Entry<Order, List<OrderItem>> entry : data.entrySet()) {
            Order orderToAdd = entry.getKey();
            orderToAdd.setOrderItems(entry.getValue());
            orders.add(orderToAdd);
        }
        return orders;
    }
}
