package com.es.core.validator;

import com.es.core.quickOrder.QuickOrderElement;
import com.es.core.quickOrder.QuickOrderElementsDto;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Service
public class QuickOrderValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return QuickOrderElementsDto.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        QuickOrderElementsDto quickOrderElementsDto = (QuickOrderElementsDto) o;
        for (QuickOrderElement quickOrderElement : quickOrderElementsDto.getQuickOrderElements()) {
            Long index = quickOrderElement.getIndex();
            String model = quickOrderElement.getModel();
            String quantity = quickOrderElement.getQuantity();
            if (model.isEmpty() && quantity.isEmpty()) {
                continue;
            }
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "quickOrderElements[" + index + "].model",
                    "errors.emptyField",
                    "Empty model");
            try {
                long quanityNum = Long.parseLong(quantity);
                if (quanityNum <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                errors.rejectValue("quickOrderElements[" + index + "].quantity", "errors.quantity.notANum",
                        "Not a number or less then 0");
            }

        }
    }
}
