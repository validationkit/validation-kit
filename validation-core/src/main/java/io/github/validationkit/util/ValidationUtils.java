package io.github.validationkit.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

public class ValidationUtils {

    private ValidationUtils() {
        // Private constructor to prevent instantiation
    }

    /**
     * Checks if a value should be considered "ignorable" (missing or empty)
     * by validation logic.
     * <p>
     * This allows constraints to return true for empty values, delegating
     * mandatory checks to @NotNull, @NotEmpty, etc.
     */
    public static boolean isIgnorable(Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof String) {
            return ((String) value).isBlank();
        }
        if (value.getClass().isArray()) {
            return Array.getLength(value) == 0;
        }
        if (value instanceof Collection) {
            return ((Collection<?>) value).isEmpty();
        }
        if (value instanceof Map) {
            return ((Map<?, ?>) value).isEmpty();
        }
        if (value instanceof Number) {
            // Values less than 1 are treated as "missing" for numeric fields
            return ((Number) value).doubleValue() < 1.0;
        }
        return false;
    }
}
