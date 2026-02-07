package io.github.validationkit.validators;

import io.github.validationkit.constraints.FileExtension;

import java.lang.annotation.Annotation;

/**
 * Mock implementation of FileExtension for unit testing.
 * Bypasses Mockito's difficulty with mocking annotation interfaces on recent
 * JDKs.
 */
@SuppressWarnings("all")
class FileExtensionMock implements FileExtension {

    private final String[] value;
    private final boolean caseSensitive;

    public FileExtensionMock(String[] value, boolean caseSensitive) {
        this.value = value;
        this.caseSensitive = caseSensitive;
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
    public String message() {
        return "mock message";
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
    public Class<? extends Annotation> annotationType() {
        return FileExtension.class;
    }
}
