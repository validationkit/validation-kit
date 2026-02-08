package io.github.validationkit.constraints;

import io.github.validationkit.validators.StrongPasswordValidator;
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
 * Validates that the password meets configured complexity requirements.
 * <p>
 * Configurable attributes:
 * <ul>
 * <li>{@code min} - minimum length (default 8)</li>
 * <li>{@code max} - maximum length (default unlimited)</li>
 * <li>{@code hasUppercase} - requires at least one uppercase letter (default
 * true)</li>
 * <li>{@code hasLowercase} - requires at least one lowercase letter (default
 * true)</li>
 * <li>{@code hasDigit} - requires at least one digit (default true)</li>
 * <li>{@code hasSpecialChar} - requires at least one special character from the
 * allowed set (default true)</li>
 * <li>{@code allowedSpecialChars} - defines valid special characters (default
 * "@$!%*?&amp;_#-")</li>
 * </ul>
 *
 * @author Hrushikesh Joshi
 */
@Documented
@Constraint(validatedBy = StrongPasswordValidator.class)
@Target({ FIELD, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface StrongPassword {

    int min() default 8;

    int max() default Integer.MAX_VALUE;

    boolean hasUppercase() default true;

    boolean hasLowercase() default true;

    boolean hasDigit() default true;

    boolean hasSpecialChar() default true;

    /**
     * Set of characters considered "special".
     * Only used if {@code hasSpecialChar} is true.
     */
    String allowedSpecialChars() default "@$!%*?&_#-";

    String message() default "Password does not meet complexity requirements";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
