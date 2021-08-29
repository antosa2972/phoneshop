package com.es.core.validator;

import com.es.core.cart.PhoneArrayDto;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.stream.IntStream;

@Service
public class PhoneArrayDtoValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return PhoneArrayDto.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        PhoneArrayDto dto = (PhoneArrayDto) o;
        IntStream.of(0, dto.getQuantity().length - 1).forEach(i -> {
            try {
                long quantity = Long.parseLong(dto.getQuantity()[i]);
                if (quantity <= 0) {
                    errors.rejectValue("quantity", dto.getPhoneId()[i]);
                }
            } catch (NumberFormatException e) {
                errors.rejectValue("quantity", dto.getPhoneId()[i]);
            }
        });
    }
}
