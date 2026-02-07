package io.github.validationkit.constraints;

import io.github.validationkit.validators.AllowedValuesValidator;
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
 * Validates that the value is one of the allowed values.
 * <p>
 * Supported types:
 * <ul>
 * <li>{@code String}: Checks if the string matches any of the allowed
 * values.</li>
 * <li>{@code Collection<?>}: Checks if <b>all</b> elements in the collection
 * match any of the allowed values.</li>
 * <li>{@code Object[]}: Checks if <b>all</b> elements in the array match any of
 * the allowed values.</li>
 * <li>{@code Object}: Checks if the {@code toString()} representation matches
 * any of the allowed values.</li>
 * </ul>
 * <p>
 * Example:
 * 
 * <pre>{@code
 * @AllowedValues(value = { "admin", "user" }, message = "Invalid role")
 * private String role;
 * }</pre>
 *
 * @author Hrushikesh Joshi
 */
@Documented
@Constraint(validatedBy = AllowedValuesValidator.class)
@Target({ FIELD, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface AllowedValues {
    /**
     * The array of allowed values.
     */
    String[] value();

    /**
     * Whether the check should be case-sensitive.
     * Default is {@code true}.
     */
    boolean caseSensitive() default true;

    /**
     * Whether to treat {@code null} as valid.
     * Default is {@code true} (standard Bean Validation behavior).
     * Use {@code @NotNull} to reject nulls.
     */
    boolean acceptNull() default true;

    String message() default "Must be one of {value}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
