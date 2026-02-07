package io.github.validationkit.starter.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "validation.errors")
public class ValidationProperties {

    /**
     * Whether to enable the default global exception handler for validation errors.
     */
    private boolean enabled = true;

    /**
     * Whether to include the list of allowed values in the error message.
     * Use with caution as this may expose sensitive data (e.g. valid user roles).
     */
    private boolean includeAllowedValues = false;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isIncludeAllowedValues() {
        return includeAllowedValues;
    }

    public void setIncludeAllowedValues(boolean includeAllowedValues) {
        this.includeAllowedValues = includeAllowedValues;
    }
}
