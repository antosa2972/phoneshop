package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import com.es.core.quickOrder.QuickOrderElement;
import com.es.core.quickOrder.QuickOrderElementsDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/quickOrder")
public class QuickOrderController {

    @Resource
    private HttpSession httpSession;

    @Resource
    private PhoneDao jdbcPhoneDao;

    @Resource
    private CartService httpSessionCartService;

    @Resource(name = "quickOrderValidator")
    private Validator quickOrderValidator;

    @RequestMapping(method = RequestMethod.GET)
    public String getQuickOrderPage(Model model) {
        model.addAttribute("success",model.asMap().get("success"));
        model.addAttribute("quickOrderElementsDto", new QuickOrderElementsDto());
        return "quickOrder";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String makeQuickOrder(@ModelAttribute QuickOrderElementsDto quickOrderElementsDto,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        Cart cart = httpSessionCartService.getCart(httpSession);
        quickOrderValidator.validate(quickOrderElementsDto, bindingResult);
        List<Phone> phoneList = new ArrayList<>();
        quickOrderElementsDto.getQuickOrderElements().forEach(quickOrderElement -> {
            int indexInList = quickOrderElementsDto.getQuickOrderElements().indexOf(quickOrderElement);
            if (!bindingResult.hasFieldErrors("quickOrderElements[" + indexInList + "].phoneModel")
                    && !bindingResult.hasFieldErrors("quickOrderElements[" + indexInList + "].quantity")) {
                String quantity = quickOrderElement.getQuantity();
                String modelPhone = quickOrderElement.getModel();
                if (quantity.isEmpty() && modelPhone.isEmpty()) {
                    return;
                }
                Phone phone = jdbcPhoneDao.get(modelPhone).orElse(null);
                if (phone != null) {
                    try {
                        httpSessionCartService.addPhone(phone.getId(), Long.parseLong(quantity), cart);
                        quickOrderElementsDto.getQuickOrderElements().set(indexInList, new QuickOrderElement());
                    } catch (OutOfStockException e) {
                        bindingResult.rejectValue("quickOrderElements[" + indexInList + "].quantity",
                                "errors.quantity.outOfStock","Out of stock!");
                    }
                    phoneList.add(phone);
                } else {
                    bindingResult.rejectValue("quickOrderElements[" + indexInList + "].model",
                            "errors.product.notFound","No such product");
                }
            }
        });
        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("successfulPhones",phoneList);
            model.addAttribute("errors", bindingResult);
            return "quickOrder";
        }
        redirectAttributes.addFlashAttribute("success",true);
        return "redirect:quickOrder";
    }

}
