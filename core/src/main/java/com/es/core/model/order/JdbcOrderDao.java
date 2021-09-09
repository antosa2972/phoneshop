package com.es.core.model.order;

import com.es.core.model.phone.PhoneDao;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Component
public class JdbcOrderDao implements OrderDao {

    public static final String SELECT_ORDER_SQL = "select * from orders left join orderItems on orders.id = orderItems.orderId where orders.id= ";
    public static final String INSERT_ORDER_SQL = " insert into orders(id,subtotal,deliveryPrice,totalPrice,firstName," +
            "lastName,deliveryAddress,contactPhoneNo,additionalInfo,status) values (:id,:subtotal,:deliveryPrice," +
            ":totalPrice,:firstName,:lastName,:deliveryAddress,:contactPhoneNo,:additionalInfo,:status)";
    public static final String SQL_INSERT_INTO_ORDER_ITEMS = "insert into orderItems (phoneId,orderId,quantity) " +
            "values (?,?,?)";
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Resource
    private PhoneDao jdbcPhoneDao;

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private OrderResultSetExtractor orderResultSetExtractor;

    @Override
    public Optional<Order> get(Long key) {
        String query = SELECT_ORDER_SQL + key;
        return Optional.ofNullable(jdbcTemplate.query(query, orderResultSetExtractor));
    }

    @Override
    public Long save(Order order) {
        MapSqlParameterSource in = new MapSqlParameterSource();
        in.addValue("id", order.getId());
        in.addValue("subtotal", order.getSubtotal());
        in.addValue("deliveryPrice", order.getDeliveryPrice());
        in.addValue("totalPrice", order.getTotalPrice());
        in.addValue("firstName", order.getFirstName());
        in.addValue("lastName", order.getLastName());
        in.addValue("deliveryAddress", order.getDeliveryAddress());
        in.addValue("contactPhoneNo", order.getContactPhoneNo());
        in.addValue("additionalInfo", order.getAdditionalInfo());
        in.addValue("status", order.getStatus().name());
        namedParameterJdbcTemplate.update(INSERT_ORDER_SQL, in);

        List<OrderItem> orderItemList = order.getOrderItems();
        orderItemList.forEach(orderItem -> jdbcTemplate
                .update(SQL_INSERT_INTO_ORDER_ITEMS, orderItem.getPhone().getId(), order.getId(), orderItem.getQuantity()));
        return order.getId();
    }
}
