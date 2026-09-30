package io.github.validationkit.constraints;

import io.github.validationkit.validators.UUIDStringValidator;
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
 * Validates that the annotated string is a valid UUID string representation.
 * <p>
 * Example:
 * <pre>{@code
 * @UUIDString(message = "Invalid UUID format")
 * private String userId;
 * }</pre>
 *
 * @author Hrushikesh Joshi
 */
@Documented
@Constraint(validatedBy = UUIDStringValidator.class)
@Target({ FIELD, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface UUIDString {

    /**
     * Optional UUID version restriction (e.g. 4 for UUIDv4).
     * Default is 0 (allows any valid UUID version 1 through 5).
     */
    int version() default 0;

    /**
     * Whether 32-character hex strings without hyphens (e.g. "123e4567e89b12d3a456426614174000") are allowed.
     * Default is false (requires standard 8-4-4-4-12 hyphenated format).
     */
    boolean allowWithoutHyphens() default false;

    String message() default "Invalid UUID format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
