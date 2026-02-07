package io.github.validationkit.validators;

import io.github.validationkit.constraints.Base64;
import io.github.validationkit.util.ValidationUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class Base64Validator implements ConstraintValidator<Base64, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (ValidationUtils.isIgnorable(value)) {
            return true;
        }

        try {
            java.util.Base64.getDecoder().decode(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
