package com.es.phoneshop.web.controller.pages.admin;

import com.es.core.model.order.Order;
import com.es.core.order.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping(value = "/admin/orders")
public class OrdersPageController {

    @Resource
    private OrderService orderServiceImpl;

    @RequestMapping(method = RequestMethod.GET)
    public String getCart(Model model) {
        List<Order> orders = orderServiceImpl.getOrders(Integer.MAX_VALUE, 0);
        if (orders != null) {
            model.addAttribute(orders);
        }
        return "orders";
    }
}
