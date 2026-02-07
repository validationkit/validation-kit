package io.github.validationkit.constraints;

import io.github.validationkit.validators.FileExtensionValidator;
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
 * Validates that the string (filename) ends with one of the allowed extensions.
 * <p>
 * This validator is case-insensitive by default.
 * Use {@link #caseSensitive()} to change this behavior.
 * <p>
 * Example:
 *
 * <pre>{@code
 * @FileExtension(value = { "jpg", "png", "jpeg" }, message = "Only images are allowed")
 * private String originalFilename;
 * }</pre>
 *
 * @author Hrushikesh Joshi
 */
@Documented
@Constraint(validatedBy = FileExtensionValidator.class)
@Target({ FIELD, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface FileExtension {
    /**
     * Allowed extensions (e.g. "jpg", "png").
     * Examples: {@code "pdf"}, {@code "txt"}.
     * The dot is optional in config; the validator handles it.
     */
    String[] value();

    boolean caseSensitive() default false;

    String message() default "Extension must be one of {value}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
