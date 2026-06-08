package io.github.validationkit.validators;

import io.github.validationkit.constraints.AllowedValues;
import io.github.validationkit.util.ValidationUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import java.util.Set;
import java.util.stream.Collectors;

public class AllowedValuesValidator implements ConstraintValidator<AllowedValues, Object> {

    private Set<String> allowedValues;
    private boolean caseSensitive;
    private boolean acceptNull;

    @Override
    public void initialize(AllowedValues constraintAnnotation) {
        this.caseSensitive = constraintAnnotation.caseSensitive();
        this.acceptNull = constraintAnnotation.acceptNull();

        Set<String> values = new HashSet<>();

        if (constraintAnnotation.value() != null) {
            values.addAll(Arrays.asList(constraintAnnotation.value()));
        }

        if (constraintAnnotation.enumClass() != null) {
            for (Class<? extends Enum<?>> enumClazz : constraintAnnotation.enumClass()) {
                for (Enum<?> enumConstant : enumClazz.getEnumConstants()) {
                    values.add(enumConstant.name());
                }
            }
        }

        if (values.isEmpty()) {
            throw new jakarta.validation.ConstraintDeclarationException(
                    "@AllowedValues requires at least one value in 'value' or 'enumClass'");
        }

        if (this.caseSensitive) {
            this.allowedValues = values;
        } else {
            this.allowedValues = values.stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toSet());
        }
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return acceptNull;
        }

        // If the value is "effectively empty", we return true (valid).
        // This prevents double errors when used with @NotBlank, @NotEmpty, @Min(1) etc.
        if (ValidationUtils.isIgnorable(value)) {
            return true;
        }

        if (value instanceof Collection<?>) {
            return ((Collection<?>) value).stream()
                    .allMatch(this::isStringAllowed);
        }

        if (value.getClass().isArray()) {
            // TODO:
            // We use Arrays.stream((Object[]) value) because primitive arrays
            // like int[] cannot be cast to Object[].
            // However, handling primitive arrays generically is complex.
            // For now, we support Object[] (String[], Integer[], etc.).
            // Primitive arrays will fall through to isStringAllowed(array) which
            // essentially calls array.toString() -> "[I@...".
            // If full primitive support is needed, we need reflection.
            if (value instanceof Object[]) {
                return Arrays.stream((Object[]) value)
                        .allMatch(this::isStringAllowed);
            }
        }

        return isStringAllowed(value);
    }

    private boolean isStringAllowed(Object value) {
        if (value == null || ValidationUtils.isIgnorable(value)) {
            return true;
        }

        String stringValue = value.toString();
        if (!caseSensitive) {
            stringValue = stringValue.toLowerCase();
        }

        return allowedValues.contains(stringValue);
    }

}
