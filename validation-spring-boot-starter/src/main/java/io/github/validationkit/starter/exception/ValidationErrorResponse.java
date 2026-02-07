package io.github.validationkit.starter.exception;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorResponse {
    private int status;
    private String message;
    private List<ValidationError> errors = new ArrayList<>();

    public ValidationErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public void addError(String field, String code, String message) {
        this.errors.add(new ValidationError(field, code, message));
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

    public static class ValidationError {
        private String field;
        private String code;
        private String message;

        public ValidationError(String field, String code, String message) {
            this.field = field;
            this.code = code;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
}
