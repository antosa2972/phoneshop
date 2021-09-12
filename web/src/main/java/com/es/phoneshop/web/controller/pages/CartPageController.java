package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import com.es.core.cart.PhoneArrayDto;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import com.es.core.validator.PhoneArrayDtoValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/cart")
public class CartPageController {
    @Resource
    private CartService cartService;
    @Resource
    private HttpSession httpSession;
    @Resource
    private PhoneDao jdbcPhoneDao;
    @Resource
    private PhoneArrayDtoValidator phoneArrayDtoValidator;

    @RequestMapping(method = RequestMethod.GET)
    public String getCart(@RequestParam(value = "successDelete", required = false) boolean successDelete,
                          @RequestParam(value = "successUpdate", required = false) boolean successUpdate,
                          @RequestParam(value = "isOutOfStock", required = false) boolean isOutOfStock,
                          @RequestParam(value = "error", required = false) boolean error,
                          @RequestParam(value = "errorsId", required = false) List<Long> errorsId,
                          Model model) {
        Cart cart = cartService.getCart(httpSession);

        model.addAttribute("successDelete",successDelete);
        model.addAttribute("successUpdate",successUpdate);
        model.addAttribute("isOutOfStock",isOutOfStock);
        model.addAttribute("error",error);
        model.addAttribute("errorsId",errorsId);
        model.addAttribute("cart", cart);

        return "cart";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String updateCart(@Validated @ModelAttribute(name = "phoneArrayDto") PhoneArrayDto phoneArrayDto, Model model, BindingResult bindingResult) {
        Cart cart = cartService.getCart(httpSession);
        if (cart.getCartItems().isEmpty()) {
            return prepareModelForEmptyCart(cart, model);
        }
        phoneArrayDtoValidator.validate(phoneArrayDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return validationFailed(cart, bindingResult, model);
        }
        HashMap<Long, Long> idAndQuantity = new HashMap<>();
        for (int i = 0; i < phoneArrayDto.getQuantity().length; i++) {
            idAndQuantity.put(Long.parseLong(phoneArrayDto.getPhoneId()[i]),
                    Long.parseLong(phoneArrayDto.getQuantity()[i]));
        }
        cartService.update(idAndQuantity, cart);
        model.addAttribute("successUpdate",true);
        model.addAttribute("cart", cart);
        return "redirect:/cart";
    }

    @RequestMapping(value = "{id}", method = RequestMethod.POST)
    public String deleteFromCart(@PathVariable("id") Long id, Model model) throws IllegalArgumentException {
        Optional<Phone> optionalPhone = jdbcPhoneDao.get(id);
        Cart cart = cartService.getCart(httpSession);
        if (cart.getCartItems().isEmpty()) {
            return prepareModelForEmptyCart(cart, model);
        }
        if (optionalPhone.isPresent()) {
            cartService.remove(id, cart);
            model.addAttribute("successDelete", true);
            model.addAttribute("cart", cart);
        } else {
            model.addAttribute("error", true);
            throw new IllegalArgumentException(String.valueOf(id));
        }
        return "redirect:/cart";
    }

    private String prepareModelForEmptyCart(Cart cart, Model model) {
        model.addAttribute("error", true);
        model.addAttribute("cart", cart);
        return "redirect:/cart";
    }

    private String validationFailed(Cart cart, BindingResult bindingResult, Model model) {
        List<FieldError> errorList = bindingResult.getFieldErrors("quantity");
        List<Long> errorsId = errorList.stream()
                .map(item -> Long.parseLong(item.getCode()))
                .collect(Collectors.toList());
        model.addAttribute("cart", cart);
        model.addAttribute("errorsId", errorsId);
        model.addAttribute("error", true);
        return "redirect:/cart";
    }
}
