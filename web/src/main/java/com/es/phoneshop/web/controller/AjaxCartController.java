package com.es.phoneshop.web.controller;

import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import com.es.core.cart.PhoneDto;
import com.es.core.exception.OutOfStockException;
import com.es.core.validator.ResponseErrors;
import com.es.core.validator.ValidationErrors;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import static org.springframework.http.ResponseEntity.badRequest;

@Controller
@RequestMapping(value = "/ajaxCart")
public class AjaxCartController {

    @Resource(name="phoneDtoValidator")
    private Validator quantityValidator;

    @Resource
    private CartService cartService;

    @Resource
    private HttpSession httpSession;

    @RequestMapping(method = RequestMethod.POST,consumes = "application/json")
    public ResponseEntity<?> addPhone(@Validated @RequestBody PhoneDto phoneDto, BindingResult bindingResult) {
        quantityValidator.validate(phoneDto,bindingResult);
        if(bindingResult.hasErrors()){
            ValidationErrors validationErrors = new ValidationErrors(bindingResult.getAllErrors());
            return badRequest().body(validationErrors.getErrors().get(0).getCode());
        }
        try {
            Cart currentCart = cartService.getCart(httpSession);
            cartService.addPhone(phoneDto.getId(), phoneDto.getQuantity(), currentCart);
            return ResponseEntity.ok(cartService.getCart(httpSession));
        } catch (OutOfStockException | IllegalArgumentException e) {
            ResponseErrors errors = new ResponseErrors(e.getMessage());
            return ResponseEntity.badRequest().body(errors);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
