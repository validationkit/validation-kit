package io.github.validationkit.validators;

import io.github.validationkit.constraints.StrongPassword;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.annotation.Annotation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

class StrongPasswordValidatorTest {

    private StrongPasswordValidator validator;

    @Mock
    private ConstraintValidatorContext context;
    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder builder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new StrongPasswordValidator();

        // Default configuration: min=8, all checks true
        validator.initialize(createAnnotation(8, Integer.MAX_VALUE, true, true, true, true, "@$!%*?&_#-"));

        // Mock context to accept custom messages
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
        when(builder.addConstraintViolation()).thenReturn(context);
    }

    @Test
    void testValidPassword() {
        // Valid: >8 chars, Upper, Lower, Number, Special
        assertTrue(validator.isValid("Password123!", context));
    }

    @Test
    void testTooShort() {
        assertFalse(validator.isValid("Pass1!", context));
    }

    @Test
    void testMissingUppercase() {
        assertFalse(validator.isValid("password123!", context));
    }

    @Test
    void testMissingLowercase() {
        assertFalse(validator.isValid("PASSWORD123!", context));
    }

    @Test
    void testMissingDigit() {
        assertFalse(validator.isValid("Password!", context));
    }

    @Test
    void testMissingSpecialChar() {
        assertFalse(validator.isValid("Password123", context));
    }

    @Test
    void testCustomSpecialChars() {
        // Only allow '#' as special char
        validator.initialize(createAnnotation(8, Integer.MAX_VALUE, true, true, true, true, "#"));

        assertFalse(validator.isValid("Password123!", context)); // '!' not allowed
        assertTrue(validator.isValid("Password123#", context)); // '#' is allowed
    }

    @Test
    void testDisabledChecks() {
        // Only checking length
        validator.initialize(createAnnotation(5, Integer.MAX_VALUE, false, false, false, false, ""));

        assertFalse(validator.isValid("pass", context)); // length 4 < 5
        assertTrue(validator.isValid("passw", context)); // length 5
    }

    @Test
    void testLengthMessage() {
        // 1. Unbounded max
        validator.initialize(createAnnotation(8, Integer.MAX_VALUE, true, true, true, true, "@"));
        validator.isValid("short", context);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(context).buildConstraintViolationWithTemplate(captor.capture());
        assertTrue(captor.getValue().contains("must be at least 8 characters"));

        // 2. Bounded max
        Mockito.clearInvocations(context); // Clear previous interactions

        validator.initialize(createAnnotation(8, 20, true, true, true, true, "@"));
        validator.isValid("short", context);

        verify(context).buildConstraintViolationWithTemplate(captor.capture());
        assertTrue(captor.getValue().contains("between 8 and 20 characters"));
    }

    @Test
    void testNullValid() {
        assertTrue(validator.isValid(null, context));
    }

    // Helper to create annotation instance
    private StrongPassword createAnnotation(int min, int max, boolean hasUpper, boolean hasLower, boolean hasDigit,
            boolean hasSpecial, String allowedSpecials) {
        return new StrongPasswordMock(min, max, hasUpper, hasLower, hasDigit, hasSpecial, allowedSpecials);
    }

    // Mock Annotation implementation
    private static class StrongPasswordMock implements StrongPassword {
        private final int min;
        private final int max;
        private final boolean hasUpper;
        private final boolean hasLower;
        private final boolean hasDigit;
        private final boolean hasSpecial;
        private final String allowedSpecials;

        public StrongPasswordMock(int min, int max, boolean hasUpper, boolean hasLower, boolean hasDigit,
                boolean hasSpecial, String allowedSpecials) {
            this.min = min;
            this.max = max;
            this.hasUpper = hasUpper;
            this.hasLower = hasLower;
            this.hasDigit = hasDigit;
            this.hasSpecial = hasSpecial;
            this.allowedSpecials = allowedSpecials;
        }

        @Override
        public int min() {
            return min;
        }

        @Override
        public int max() {
            return max;
        }

        @Override
        public boolean hasUppercase() {
            return hasUpper;
        }

        @Override
        public boolean hasLowercase() {
            return hasLower;
        }

        @Override
        public boolean hasDigit() {
            return hasDigit;
        }

        @Override
        public boolean hasSpecialChar() {
            return hasSpecial;
        }

        @Override
        public String allowedSpecialChars() {
            return allowedSpecials;
        }

        @Override
        public String message() {
            return "error";
        }

        @Override
        public Class<?>[] groups() {
            return new Class[0];
        }

        @SuppressWarnings("unchecked")
        @Override
        public Class<? extends jakarta.validation.Payload>[] payload() {
            return (Class<? extends jakarta.validation.Payload>[]) new Class[0];
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return StrongPassword.class;
        }
    }
}
