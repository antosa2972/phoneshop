package com.es.phoneshop.web.controller.pages;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.es.core.cart.CartService;
import com.es.core.filter.FilterService;
import com.es.core.model.phone.Phone;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.es.core.model.phone.PhoneDao;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping(value = "/productList")
public class ProductListPageController {
    public static final Long QUANTITY_ON_PAGE = 20L;
    @Resource
    private PhoneDao phoneDao;
    @Resource
    private CartService cartService;
    @Resource
    private FilterService filterService;
    @Resource
    private HttpSession httpSession;

    @RequestMapping(method = RequestMethod.GET)
    public String showProductList(@RequestParam(required = false) String search,
                                  @RequestParam(required = false) String field,
                                  @RequestParam(required = false) String order,
                                  @RequestParam(required = false) Long page, Model model) {
        if (page == null) {
            page = 1L;
        }
        field = filterService.checkFieldVal(field);
        order = filterService.checkOrderVal(order);
        List<Phone> phoneList = phoneDao.findAll(search, field, order,((Long) ((page - 1) * QUANTITY_ON_PAGE)).intValue(),
                QUANTITY_ON_PAGE.intValue());

        Long phoneQuantity = phoneDao.count(search, field, order,((Long) ((page - 1) * QUANTITY_ON_PAGE)).intValue(),
                QUANTITY_ON_PAGE.intValue());

        long numOfPages = phoneQuantity / QUANTITY_ON_PAGE;

        long lastPage;
        if (phoneQuantity % QUANTITY_ON_PAGE != 0) {
            lastPage = numOfPages + 1;
        } else {
            lastPage = numOfPages;
        }
        model.addAttribute("phones", phoneList);
        model.addAttribute("cart", cartService.getCart(httpSession));
        model.addAttribute("pages", lastPage);
        model.addAttribute("phoneQuantity", phoneQuantity);
        return "productList";
    }
}
