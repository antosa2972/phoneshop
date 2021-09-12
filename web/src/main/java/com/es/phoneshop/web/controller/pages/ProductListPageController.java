package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.CartService;
import com.es.core.model.ParamsForSearch;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import com.es.core.sortenums.SortField;
import com.es.core.sortenums.SortOrder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping(value = "/productList")
public class ProductListPageController {
    public static final Long QUANTITY_ON_PAGE = 20L;
    @Resource
    private PhoneDao phoneDao;
    @Resource
    private CartService cartService;
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
        if (field != null && order != null) {
            try {
                field = SortField.valueOf(field.toUpperCase(Locale.ROOT)).name();
                order = SortOrder.valueOf(order.toUpperCase(Locale.ROOT)).name();
            } catch (IllegalArgumentException e) {
                field = null;
                order = null;
            }
        }
        long offset = (page - 1) * QUANTITY_ON_PAGE;
        ParamsForSearch paramsForSearch = new ParamsForSearch(search, field, order, (int) offset,
                QUANTITY_ON_PAGE.intValue());
        List<Phone> phoneList = phoneDao.findAll(paramsForSearch);

        Long phoneQuantity = phoneDao.count(paramsForSearch);

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
