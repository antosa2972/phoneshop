package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.CartService;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping(value = "/productDetails/{id}")
public class ProductDetailsPageController {
    @Resource
    PhoneDao jdbcPhoneDao;
    @Resource
    CartService httpSessionCartService;
    @Resource
    HttpSession httpSession;

    @RequestMapping(method = RequestMethod.GET)
    public String showProductDetailsInfo(@PathVariable("id") Long phoneId, Model model){
        Optional<Phone> optionalPhone = jdbcPhoneDao.get(phoneId);
        Phone phone = null;
        if(optionalPhone.isPresent()){
            phone = optionalPhone.get();
        }
        model.addAttribute("phone",phone);
        model.addAttribute("cart", httpSessionCartService.getCart(httpSession));
        return "productDetails";
    }
}
