package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import com.es.core.cart.PhoneArrayDto;
import com.es.core.exception.NoElementWithSuchIdException;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import com.es.core.validator.PhoneArrayDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
    @Autowired
    private Environment environment;
    @Resource
    private PhoneArrayDtoValidator phoneArrayDtoValidator;

    private @Value("#{messages['deleteFromCartMsg']}")
    String successMessage;
    private @Value("#{messages['emptyCartMsg']}")
    String emptyCartMsg;
    private @Value("#{messages['updateCartMessageError']}")
    String updateCartMessageError;
    private @Value("#{messages['updateCartMessageSuccess']}")
    String updateCartMessageSuccess;

    @RequestMapping(method = RequestMethod.GET)
    public String getCart(Model model) {
        Cart cart = cartService.getCart(httpSession);
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
        List<Phone> outOfStockPhones = cartService.update(idAndQuantity, cart);
        if (outOfStockPhones.isEmpty()) {
            model.addAttribute("message", updateCartMessageSuccess);
        } else {
            model.addAttribute("outOfStockPhones", outOfStockPhones);
            model.addAttribute("error",updateCartMessageError);
        }
        model.addAttribute("cart",cart);
        return "cart";
    }

    @RequestMapping(value = "{id}", method = RequestMethod.POST)
    public String deleteFromCart(@PathVariable("id") Long id, Model model) throws NoElementWithSuchIdException {
        Optional<Phone> optionalPhone = jdbcPhoneDao.get(id);
        Cart cart = cartService.getCart(httpSession);
        if (cart.getCartItems().isEmpty()) {
            return prepareModelForEmptyCart(cart, model);
        }
        if (optionalPhone.isPresent()) {
            cartService.remove(id, cart);
            model.addAttribute("message", successMessage);
            model.addAttribute("cart", cart);
        } else {
            throw new NoElementWithSuchIdException(id);
        }

        return "cart";
    }

    private String prepareModelForEmptyCart(Cart cart, Model model) {
        model.addAttribute("error", environment.getProperty(emptyCartMsg));
        model.addAttribute("cart", cart);
        return "cart";
    }

    private String validationFailed(Cart cart, BindingResult bindingResult, Model model) {
        List<FieldError> errorList = bindingResult.getFieldErrors("quantity");
        List<Long> errorsId = errorList.stream()
                .map(item -> Long.parseLong(item.getCode()))
                .collect(Collectors.toList());
        model.addAttribute("cart", cart);
        model.addAttribute("errorsId", errorsId);
        model.addAttribute("error", updateCartMessageError);
        return "cart";
    }
}
