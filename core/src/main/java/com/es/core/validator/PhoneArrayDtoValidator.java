package com.es.core.validator;

import com.es.core.cart.PhoneArrayDto;
import com.es.core.model.phone.Stock;
import com.es.core.model.phone.StockDao;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.annotation.Resource;


@Service
public class PhoneArrayDtoValidator implements Validator {
    @Resource
    private StockDao jdbcStockDao;

    @Override
    public boolean supports(Class<?> aClass) {
        return PhoneArrayDto.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        PhoneArrayDto dto = (PhoneArrayDto) o;
        for (int i = 0; i < dto.getQuantity().length; i++) {
            try {
                Stock stock = jdbcStockDao.get(Long.parseLong(dto.getPhoneId()[i])).orElse(null);
                long quantity = Long.parseLong(dto.getQuantity()[i]);
                if (quantity <= 0 || stock == null || quantity > (stock.getStock() - stock.getReserved())) {
                    errors.rejectValue("quantity", dto.getPhoneId()[i]);
                }
            } catch (NumberFormatException e) {
                errors.rejectValue("quantity", dto.getPhoneId()[i]);
            }
        }
    }
}
