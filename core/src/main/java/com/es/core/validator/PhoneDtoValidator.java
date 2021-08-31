package com.es.core.validator;

import com.es.core.cart.PhoneDto;
import com.es.core.model.phone.Stock;
import com.es.core.model.phone.StockDao;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import javax.annotation.Resource;

@Service
public class PhoneDtoValidator implements Validator {

    @Resource
    private StockDao jdbcStockDao;

    @Override
    public boolean supports(Class<?> aClass) {
        return PhoneDto.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "quantity", "Empty quantity");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "id", "Empty id");
        if (!errors.hasErrors()) {
            PhoneDto dto = (PhoneDto) o;
            Stock stock = jdbcStockDao.get(dto.getId()).orElse(null);
            if (dto.getQuantity() <= 0) {
                errors.rejectValue("quantity", "Wrong input");
            }
            if(stock == null || dto.getQuantity() > (stock.getStock() - stock.getReserved())){
                errors.rejectValue("quantity","Out of stock!");
            }
        }
    }
}
