package com.es.phoneshop.web.controller.pages.admin;

import com.es.core.model.order.OrderDao;
import com.es.core.model.order.OrderStatus;
import com.es.core.order.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/admin/orders/{id}")
public class OrderOverviewPageControllerAdmin {

    @Resource
    private OrderDao jdbcOrderDao;
    @Resource
    private OrderService orderServiceImpl;

    @RequestMapping(method = RequestMethod.GET)
    public String getOrderOverview(@PathVariable("id") Long id, Model model) {
        jdbcOrderDao.get(id).ifPresent(order -> model.addAttribute("order", order));
        model.addAttribute("id", id);
        return "orderOverviewAdmin";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String changeOrderStatus(@PathVariable("id") Long orderId,
                                    @RequestParam(name = "orderStatus") String orderStatus) {
        if (OrderStatus.DELIVERED.name().equals(orderStatus)) {
            orderServiceImpl.updateStatus(OrderStatus.DELIVERED, orderId);
        } else {
            orderServiceImpl.updateStatus(OrderStatus.REJECTED, orderId);
        }
        return "redirect:/admin/orders/" + orderId;
    }

    @ExceptionHandler(RuntimeException.class)
    public String handleOutOfStock() {
        return "redirect:/404?message=" + "no such order";
    }
}
