package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import com.es.core.model.order.OrderDataDto;
import com.es.core.order.OrderService;
import com.es.core.exception.OutOfStockException;
import com.es.core.validator.OrderDataDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;

@Controller
@PropertySource("classpath:/lang-en.properties")
@RequestMapping(value = "/order")
public class OrderPageController {
    @Resource
    private OrderService orderService;
    @Resource
    private CartService cartService;
    @Resource
    private HttpSession httpSession;
    @Resource
    private OrderDataDtoValidator orderDataDtoValidator;
    @Autowired
    private Environment environment;

    @RequestMapping(method = RequestMethod.GET)
    public String getOrder(Model model) {
        model.addAttribute("errors", model.asMap().get("errors"));
        model.addAttribute("error", model.asMap().get("error"));
        Cart cart = cartService.getCart(httpSession);
        showCartAsOrder(cart, model);
        return "order";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String placeOrder(@Validated @ModelAttribute(name = "orderDataDto") OrderDataDto orderDataDto,
                             Model model,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) throws OutOfStockException {
        Cart cart = cartService.getCart(httpSession);
        if (cart.getCartItems().isEmpty()) {
            return prepareModelForEmptyCart(cart, model,redirectAttributes);
        }
        orderDataDtoValidator.validate(orderDataDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return prepareModelForValidationErrors(cart, model, bindingResult,redirectAttributes);
        }
        Long id = orderService.placeOrder(cart, orderDataDto, Long.parseLong(environment.getProperty("delivery.price")));
        cartService.deleteCart(httpSession);
        return "redirect:/orderOverview/" + id;
    }

    private String prepareModelForValidationErrors(Cart cart, Model model, BindingResult bindingResult,
                                                   RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", true);
        redirectAttributes.addFlashAttribute("errors",bindingResult);
        return "redirect:/order";
    }

    private String prepareModelForEmptyCart(Cart cart, Model model,RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", true);
        return "redirect:/order";
    }

    private void showCartAsOrder(Cart cart, Model model) {
        model.addAttribute("cart", cart);
        BigDecimal deliveryPrice = BigDecimal.valueOf(Long.parseLong(environment.getProperty("delivery.price")));
        model.addAttribute("deliveryPrice", deliveryPrice);
        model.addAttribute("totalCost", cart.getTotalCost().add(deliveryPrice));
    }

    @ExceptionHandler(OutOfStockException.class)
    public String handleOutOfStock() {
        return "redirect:/404?message=" + environment.getProperty("exception.outOfStock");
    }
}
