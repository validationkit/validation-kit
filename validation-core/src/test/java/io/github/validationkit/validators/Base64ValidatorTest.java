package io.github.validationkit.validators;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Base64ValidatorTest {

    private Base64Validator validator;

    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new Base64Validator();
    }

    @Test
    void shouldReturnTrueForNullOrEmpty() {
        assertTrue(validator.isValid(null, context));
        assertTrue(validator.isValid("", context));
        assertTrue(validator.isValid("  ", context));
    }

    @Test
    void shouldReturnTrueForValidBase64() {
        assertTrue(validator.isValid("SGVsbG8=", context)); // "Hello"
        assertTrue(validator.isValid("dGVzdA==", context)); // "test"
    }

    @Test
    void shouldReturnFalseForInvalidBase64() {
        assertFalse(validator.isValid("Hello", context)); // Not padded/valid base64
        assertFalse(validator.isValid("!!!", context)); // Special characters
        assertFalse(validator.isValid("invalid-base64", context));
    }
}
