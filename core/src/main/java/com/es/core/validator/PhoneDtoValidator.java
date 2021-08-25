package com.es.core.validator;

import com.es.core.cart.PhoneDto;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Service
public class PhoneDtoValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return PhoneDto.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "quantity", "message.empty.quantity");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "id", "message.empty.id");
    }
}
