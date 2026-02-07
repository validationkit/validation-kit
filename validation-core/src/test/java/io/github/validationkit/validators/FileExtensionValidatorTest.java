package io.github.validationkit.validators;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileExtensionValidatorTest {

    private FileExtensionValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new FileExtensionValidator();
        // Default configuration: jpg, png, pdf, caseSensitive=true
        validator.initialize(new FileExtensionMock(new String[] { "jpg", "png", "pdf" }, true));
    }

    @Test
    void shouldReturnTrueForNullOrEmpty() {
        assertTrue(validator.isValid(null, context));
        assertTrue(validator.isValid("", context));
        assertTrue(validator.isValid("   ", context));
    }

    @Test
    void shouldReturnTrueForValidExtensions() {
        assertTrue(validator.isValid("image.jpg", context));
        assertTrue(validator.isValid("document.pdf", context));
        assertTrue(validator.isValid("/path/to/file.png", context));
    }

    @Test
    void shouldReturnFalseForInvalidExtensions() {
        assertFalse(validator.isValid("file.txt", context));
        assertFalse(validator.isValid("image.gif", context));
        assertFalse(validator.isValid("no_extension_file", context));
    }

    @Test
    void shouldBeCaseSensitiveByDefault() {
        assertFalse(validator.isValid("image.JPG", context));
    }

    @Test
    void shouldSupportCaseInsensitivity() {
        // Re-initialize with caseSensitive = false
        validator.initialize(new FileExtensionMock(new String[] { "jpg", "png", "pdf" }, false));

        assertTrue(validator.isValid("image.JPG", context));
        assertTrue(validator.isValid("document.Pdf", context));
    }

    @Test
    void shouldHandleFilesWithoutExtensionGracefully() {
        assertFalse(validator.isValid("README", context));
        assertFalse(validator.isValid("docker-compose", context));
    }
}
