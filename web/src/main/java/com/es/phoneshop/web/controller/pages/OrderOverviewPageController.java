package com.es.phoneshop.web.controller.pages;

import com.es.core.exception.OutOfStockException;
import com.es.core.model.order.OrderDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/orderOverview/{id}")
public class OrderOverviewPageController {
    @Resource
    OrderDao jdbcOrderDao;

    @RequestMapping(method = RequestMethod.GET)
    public String getOrderOverview(@PathVariable("id") Long id, Model model) {
        jdbcOrderDao.get(id).ifPresent(order -> model.addAttribute("order", order));
        model.addAttribute("id",id);
        return "orderOverview";
    }
    @ExceptionHandler(RuntimeException.class)
    public String handleOutOfStock() {
        return "redirect:/404?message=" + "no such order";
    }
}
