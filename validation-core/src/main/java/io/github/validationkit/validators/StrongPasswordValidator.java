package io.github.validationkit.validators;

import io.github.validationkit.constraints.StrongPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Validator for {@link StrongPassword}.
 * <p>
 * Checks length and presence of required character types.
 * Returns the first violation message if multiple fail.
 */
public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {

    private int min;
    private int max;
    private boolean hasUppercase;
    private boolean hasLowercase;
    private boolean hasDigit;
    private boolean hasSpecialChar;
    private String allowedSpecialChars;

    @Override
    public void initialize(StrongPassword constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
        this.hasUppercase = constraintAnnotation.hasUppercase();
        this.hasLowercase = constraintAnnotation.hasLowercase();
        this.hasDigit = constraintAnnotation.hasDigit();
        this.hasSpecialChar = constraintAnnotation.hasSpecialChar();
        this.allowedSpecialChars = constraintAnnotation.allowedSpecialChars();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // null is valid, use @NotNull if needed
        }

        List<String> errors = new ArrayList<>();

        if (value.length() < min || value.length() > max) {
            if (max == Integer.MAX_VALUE) {
                errors.add(String.format("Length must be at least %d characters", min));
            } else {
                errors.add(String.format("Length must be between %d and %d characters", min, max));
            }
        }

        if (hasUppercase && !containsUppercase(value)) {
            errors.add("Must contain at least one uppercase letter");
        }

        if (hasLowercase && !containsLowercase(value)) {
            errors.add("Must contain at least one lowercase letter");
        }

        if (hasDigit && !containsDigit(value)) {
            errors.add("Must contain at least one digit");
        }

        if (hasSpecialChar && !containsSpecialChar(value, allowedSpecialChars)) {
            /*
             * Escape characters for the error message to avoid confusion.
             * We just list them as is for now.
             */
            errors.add("Must contain at least one special character from: " + allowedSpecialChars);
        }

        if (errors.isEmpty()) {
            return true;
        }

        // Disable default message and add custom messages
        context.disableDefaultConstraintViolation();

        // Join all errors
        String finalMessage = String.join(", ", errors);
        context.buildConstraintViolationWithTemplate(finalMessage).addConstraintViolation();

        return false;
    }

    private boolean containsUppercase(String value) {
        for (char c : value.toCharArray()) {
            if (Character.isUpperCase(c)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsLowercase(String value) {
        for (char c : value.toCharArray()) {
            if (Character.isLowerCase(c)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsDigit(String value) {
        for (char c : value.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsSpecialChar(String value, String allowedChars) {
        for (char c : value.toCharArray()) {
            if (allowedChars.indexOf(c) >= 0) {
                return true;
            }
        }
        return false;
    }
}
