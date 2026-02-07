package io.github.validationkit.starter.exception;

import io.github.validationkit.starter.configuration.ValidationProperties;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Set;

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class GlobalValidationExceptionHandler {

    private final ValidationProperties properties;

    public GlobalValidationExceptionHandler(ValidationProperties properties) {
        this.properties = properties;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();

        ValidationErrorResponse response = new ValidationErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed");

        for (FieldError error : result.getFieldErrors()) {
            String message = error.getDefaultMessage();
            message = applySecurityFiltering(error.getCode(), message);

            response.addError(
                    error.getField(),
                    error.getCode(),
                    message);
        }

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        ValidationErrorResponse response = new ValidationErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed");

        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            String fieldName = extractFieldName(violation.getPropertyPath());
            String code = violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName();
            String message = violation.getMessage();

            message = applySecurityFiltering(code, message);

            response.addError(
                    fieldName,
                    code,
                    message);
        }

        return ResponseEntity.badRequest().body(response);
    }

    private String applySecurityFiltering(String code, String message) {
        if (!properties.isIncludeAllowedValues()) {
            if ("AllowedValues".equals(code)) {
                return "Invalid value provided";
            } else if ("FileExtension".equals(code)) {
                return "Invalid file extension";
            }
        }
        return message;
    }

    // Extracts the last part of a property path (e.g. "searchUsers.status" ->
    // "status")
    private String extractFieldName(Path path) {
        String fieldName = "";
        for (Path.Node node : path) {
            fieldName = node.getName();
        }
        return fieldName;
    }
}
