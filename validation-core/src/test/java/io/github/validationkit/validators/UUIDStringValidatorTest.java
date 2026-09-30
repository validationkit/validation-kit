package io.github.validationkit.validators;

import io.github.validationkit.constraints.UUIDString;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class UUIDStringValidatorTest {

    private UUIDStringValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private UUIDString uuidStringAnnotation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new UUIDStringValidator();
        when(uuidStringAnnotation.version()).thenReturn(0);
        when(uuidStringAnnotation.allowWithoutHyphens()).thenReturn(false);
        validator.initialize(uuidStringAnnotation);
    }

    @Test
    void shouldReturnTrueForNullOrEmpty() {
        assertTrue(validator.isValid(null, context));
        assertTrue(validator.isValid("", context));
        assertTrue(validator.isValid("   ", context));
    }

    @Test
    void shouldReturnTrueForValidUUIDString() {
        String randomUuid = UUID.randomUUID().toString();
        assertTrue(validator.isValid(randomUuid, context));
        assertTrue(validator.isValid("123e4567-e89b-12d3-a456-426614174000", context));
        assertTrue(validator.isValid("00000000-0000-0000-0000-000000000000", context));
    }

    @Test
    void shouldReturnFalseForInvalidUUIDFormat() {
        assertFalse(validator.isValid("not-a-uuid", context));
        assertFalse(validator.isValid("123e4567-e89b-12d3-a456", context));
        assertFalse(validator.isValid("123e4567e89b12d3a456426614174000", context)); // hyphens required by default
        assertFalse(validator.isValid("123e4567-e89b-12d3-a456-42661417400g", context)); // invalid hex 'g'
        assertFalse(validator.isValid("123e4567_e89b_12d3_a456_426614174000", context)); // wrong separator
    }

    @Test
    void shouldAllowNonHyphenatedUUIDWhenConfigured() {
        when(uuidStringAnnotation.allowWithoutHyphens()).thenReturn(true);
        validator.initialize(uuidStringAnnotation);

        assertTrue(validator.isValid("123e4567e89b12d3a456426614174000", context));
        assertTrue(validator.isValid("123e4567-e89b-12d3-a456-426614174000", context));
        assertFalse(validator.isValid("123e4567e89b12d3a45642661417400", context)); // 31 chars
    }

    @Test
    void shouldValidateSpecificUUIDVersion() {
        // v4 UUID
        String v4Uuid = UUID.randomUUID().toString(); // e.g. xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx
        when(uuidStringAnnotation.version()).thenReturn(4);
        validator.initialize(uuidStringAnnotation);
        assertTrue(validator.isValid(v4Uuid, context));

        when(uuidStringAnnotation.version()).thenReturn(1);
        validator.initialize(uuidStringAnnotation);
        assertFalse(validator.isValid(v4Uuid, context));
    }
}
