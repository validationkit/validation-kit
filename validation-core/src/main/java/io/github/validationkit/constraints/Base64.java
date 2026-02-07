package io.github.validationkit.constraints;

import io.github.validationkit.validators.Base64Validator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validates that the value is a valid Base64 encoded string.
 * <p>
 * The validation uses {@link java.util.Base64#getDecoder()} to ensure the
 * string can be decoded.
 * <p>
 * Example:
 * 
 * <pre>{@code
 * @Base64(message = "Not a valid Base64 string")
 * private String encodedData;
 * }</pre>
 *
 * @author Hrushikesh Joshi
 */
@Documented
@Constraint(validatedBy = Base64Validator.class)
@Target({ FIELD, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface Base64 {

    String message() default "Invalid Base64 format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
