package io.github.validationkit.validators;

import io.github.validationkit.constraints.FileExtension;
import io.github.validationkit.util.ValidationUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class FileExtensionValidator implements ConstraintValidator<FileExtension, String> {

    private Set<String> allowedExtensions;
    private boolean caseSensitive;

    @Override
    public void initialize(FileExtension constraintAnnotation) {
        this.caseSensitive = constraintAnnotation.caseSensitive();

        this.allowedExtensions = Arrays.stream(constraintAnnotation.value())
                .map(ext -> ext.startsWith(".") ? ext.substring(1) : ext) // Strip dot if user provided it
                .map(ext -> caseSensitive ? ext : ext.toLowerCase())
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (ValidationUtils.isIgnorable(value)) {
            return true;
        }

        int lastDotIndex = value.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == value.length() - 1) {
            return false; // No extension found
        }

        String extension = value.substring(lastDotIndex + 1);

        if (!caseSensitive) {
            extension = extension.toLowerCase();
        }

        return allowedExtensions.contains(extension);
    }
}
