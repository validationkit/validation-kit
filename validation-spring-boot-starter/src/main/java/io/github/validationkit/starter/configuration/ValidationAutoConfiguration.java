package io.github.validationkit.starter.configuration;

import io.github.validationkit.starter.exception.GlobalValidationExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnWebApplication
@EnableConfigurationProperties(ValidationProperties.class)
public class ValidationAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "validation.errors", name = "enabled", havingValue = "true", matchIfMissing = true)
    public GlobalValidationExceptionHandler globalValidationExceptionHandler(ValidationProperties properties) {
        return new GlobalValidationExceptionHandler(properties);
    }
}
