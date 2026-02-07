package io.github.validationkit.validators;

import io.github.validationkit.constraints.AllowedValues;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AllowedValuesValidatorTest {

    private AllowedValuesValidator validator;

    // ConstraintValidatorContext can still be mocked as it is a standard interface
    @Mock
    private ConstraintValidatorContext context;

    @SuppressWarnings("all")
    private static class AllowedValuesMock implements AllowedValues {
        private final String[] value;
        private final boolean caseSensitive;
        private final boolean acceptNull;

        public AllowedValuesMock(String[] value, boolean caseSensitive, boolean acceptNull) {
            this.value = value;
            this.caseSensitive = caseSensitive;
            this.acceptNull = acceptNull;
        }

        @Override
        public String message() {
            return "{io.github.validationkit.constraints.AllowedValues.message}";
        }

        @Override
        public Class<?>[] groups() {
            return new Class[0];
        }

        @SuppressWarnings("unchecked")
        @Override
        public Class<? extends jakarta.validation.Payload>[] payload() {
            return (Class<? extends jakarta.validation.Payload>[]) new Class<?>[0];
        }

        @Override
        public String[] value() {
            return value;
        }

        @Override
        public boolean caseSensitive() {
            return caseSensitive;
        }

        @Override
        public boolean acceptNull() {
            return acceptNull;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return AllowedValues.class;
        }
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new AllowedValuesValidator();
        // Default configuration
        validator.initialize(new AllowedValuesMock(new String[] { "java", "python", "go" }, true, true));
    }

    @Test
    void shouldReturnTrueForNullWhenAcceptNullIsTrue() {
        assertTrue(validator.isValid(null, context));
    }

    @Test
    void shouldReturnFalseForNullWhenAcceptNullIsFalse() {
        // Re-initialize with acceptNull = false
        validator.initialize(new AllowedValuesMock(new String[] { "java", "python", "go" }, true, false));
        assertFalse(validator.isValid(null, context));
    }

    @Test
    void shouldReturnTrueForIgnorableValues() {
        assertTrue(validator.isValid("", context));
        assertTrue(validator.isValid(Collections.emptyList(), context));
        // Empty array
        assertTrue(validator.isValid(new String[] {}, context));
    }

    @Test
    void shouldValidateStringCorrectly() {
        assertTrue(validator.isValid("java", context));
        assertFalse(validator.isValid("ruby", context));
    }

    @Test
    void shouldValidateStringCaseInsensitive() {
        // Re-initialize with caseSensitive = false
        validator.initialize(new AllowedValuesMock(new String[] { "java", "python", "go" }, false, true));

        assertTrue(validator.isValid("JAVA", context));
        assertTrue(validator.isValid("PyThOn", context));
    }

    @Test
    void shouldValidateListCorrectly() {
        assertTrue(validator.isValid(List.of("java", "python"), context));
        assertFalse(validator.isValid(List.of("java", "ruby"), context)); // ruby is invalid
    }

    @Test
    void shouldValidateArrayCorrectly() {
        assertTrue(validator.isValid(new String[] { "java", "go" }, context));
        assertFalse(validator.isValid(new String[] { "java", "rust" }, context)); // rust is invalid
    }

    @Test
    void shouldValidateObjectToString() {
        // Enums or custom objects rely on toString()
        Object validObj = new Object() {
            public String toString() {
                return "java";
            }
        };
        Object invalidObj = new Object() {
            public String toString() {
                return "ruby";
            }
        };

        assertTrue(validator.isValid(validObj, context));
        assertFalse(validator.isValid(invalidObj, context));
    }
}
