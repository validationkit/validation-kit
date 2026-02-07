package io.github.validationkit.util;

import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ValidationUtilsTest {

    @Test
    void isIgnorable_shouldReturnTrueForNull() {
        assertTrue(ValidationUtils.isIgnorable(null));
    }

    @Test
    void isIgnorable_shouldReturnTrueForEmptyString() {
        assertTrue(ValidationUtils.isIgnorable(""));
        assertTrue(ValidationUtils.isIgnorable("   ")); // Blank is ignorable
    }

    @Test
    void isIgnorable_shouldReturnFalseForNonEmptyString() {
        assertFalse(ValidationUtils.isIgnorable("hello"));
    }

    @Test
    void isIgnorable_shouldReturnTrueForEmptyCollection() {
        assertTrue(ValidationUtils.isIgnorable(Collections.emptyList()));
        assertTrue(ValidationUtils.isIgnorable(List.of()));
    }

    @Test
    void isIgnorable_shouldReturnFalseForNonEmptyCollection() {
        assertFalse(ValidationUtils.isIgnorable(List.of("item")));
    }

    @Test
    void isIgnorable_shouldReturnTrueForEmptyMap() {
        assertTrue(ValidationUtils.isIgnorable(Collections.emptyMap()));
        assertTrue(ValidationUtils.isIgnorable(Map.of()));
    }

    @Test
    void isIgnorable_shouldReturnFalseForNonEmptyMap() {
        assertFalse(ValidationUtils.isIgnorable(Map.of("key", "value")));
    }

    @Test
    void isIgnorable_shouldReturnTrueForNumberLessThanOne() {
        assertTrue(ValidationUtils.isIgnorable(0));
        assertTrue(ValidationUtils.isIgnorable(-1));
        assertTrue(ValidationUtils.isIgnorable(0.99));
        assertTrue(ValidationUtils.isIgnorable(0L));
    }

    @Test
    void isIgnorable_shouldReturnFalseForNumberOneOrGreater() {
        assertFalse(ValidationUtils.isIgnorable(1));
        assertFalse(ValidationUtils.isIgnorable(100));
        assertFalse(ValidationUtils.isIgnorable(1.5));
    }

    @Test
    void isIgnorable_shouldReturnTrueForEmptyArray() {
        assertTrue(ValidationUtils.isIgnorable(new String[] {}));
        assertTrue(ValidationUtils.isIgnorable(new Integer[] {}));
        assertTrue(ValidationUtils.isIgnorable(new int[] {})); // Primitives array length check
    }

    @Test
    void isIgnorable_shouldReturnFalseForNonEmptyArray() {
        assertFalse(ValidationUtils.isIgnorable(new String[] { "a" }));
        assertFalse(ValidationUtils.isIgnorable(new int[] { 1 }));
    }
}
