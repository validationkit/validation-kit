# Validation Kit

[![Maven Central](https://img.shields.io/maven-central/v/io.github.validationkit/validation-spring-boot-starter.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.validationkit/validation-spring-boot-starter)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java 17+](https://img.shields.io/badge/Java-17%2B-blue)](https://adoptium.net/)

## Why Validation Kit?

<p>Ever found yourself or your team writing the same API payload validation logic again and again in each microservice? <br> Well, this library fixes that, it works hand-in-hand with your existing Hibernate Validator setup but gives you some extra constraints to make sure your API payload is valid. <br> Validation execution is fully handled by Hibernate Validator; Validation Kit only provides additional reusable constraints.</p>

**Use Validation Kit when:**
- You want reusable DTO/API payload validation constraints
- Multiple services need consistent validation rules

**Validation Kit is NOT intended for:**
- Business rule validation
- Domain invariants
- Replacing Jakarta Bean Validation

## Installation

Add the dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.validationkit</groupId>
    <artifactId>validation-spring-boot-starter</artifactId>
    <version>0.0.2</version>
</dependency>
```

## Quick Start

### 1. Define your DTO
Combine standard Jakarta annotations (like `@NotBlank`) with Validation Kit extensions:

```java
public class UserRequest {

    @NotBlank
    private String username;

    @AllowedValues(value = {"admin", "user", "guest"}, message = "Role must be one of {value}")
    private String role;

    @FileExtension(value = {"jpg", "png"})
    private String profileImage;
}
```

### 2. Use in Controller

```java
@PostMapping("/users")
public void createUser(@Valid @RequestBody UserRequest request) {
    // ...
}
```

### 3. (Optional) Configuration

In `application.yml`:

```yaml
validation:
  errors:
    enabled: true # Enable the global exception handler
    include-allowed-values: false # Set to true to include the [admin, user, guest] list in the error message
```

## Error Response Format

> This applies only if you are using provided error handler, which is optional

Note: the naming of fields in this error response will not change in future so your code which consumes this response will not break. But note that we may need to introduce additional attributes/fields to give more details, please write your code in such a way that it doesn't break with new fields.

```json
{
  "status": 400,
  "message": "Validation failed",
  "errors": [
    {
      "field": "role",
      "code": "AllowedValues",
      "message": "Role must be one of {value}"
    }
  ]
}
```

## Annotation Reference

### `@AllowedValues`
Validates that a field is one of the allowed strings.
- **Supported types:** `String`, `Object` (toString), `Collection<?>`, `Object[]`.

```java
@AllowedValues(value = {"admin", "user"}, caseSensitive = false)
private String role;
```

**Attributes:**
- `value` (required): Array of allowed string values.
- `caseSensitive` (default: `true`): Whether the check matches case.
- `acceptNull` (default: `true`): Whether `null` is considered valid (standard Bean Validation behavior). Use `@NotNull` to reject nulls.

### `@Base64`
Validates that a string is a valid Base64 encoded sequence using `java.util.Base64`.

```java
@Base64
private String encodedData;
```

### `@FileExtension`
Validates that a string (filename) ends with one of the allowed extensions.

```java
@FileExtension(value = {"jpg", "png", "pdf"}, caseSensitive = false)
private String filename;
```

**Attributes:**
- `value` (required): Array of allowed extensions (e.g., "jpg", "pdf").
- `caseSensitive` (default: `false`): Whether checking against the extension list is case-sensitive.

### `@StrongPassword`
Validates password complexity with configurable rules.

```java
public class PasswordRequest {

    // Default: min=8, requires uppercase, lowercase, digit, special char
    @StrongPassword
    private String password;

    // Custom configuration
    @StrongPassword(
        min = 12, 
        hasSpecialChar = false, 
        hasDigit = true, 
        allowedSpecialChars = "@#",
        message = "Password criteria not met"
    )
    private String pin;
}
```

**Attributes:**
- `min` (default: `8`): Minimum length.
- `max` (default: `Integer.MAX_VALUE`): Maximum length.
- `hasUppercase` (default: `true`): Requires at least one uppercase letter.
- `hasLowercase` (default: `true`): Requires at least one lowercase letter.
- `hasDigit` (default: `true`): Requires at least one digit.
- `hasSpecialChar` (default: `true`): Requires at least one special character.
- `allowedSpecialChars` (default: `"@$!%*?&_#-"`): The set of allowed special characters (used only if `hasSpecialChar` is true).

## Motivation
This project originated from microservice environments where identical validation rules were repeatedly implemented across services, leading to drift and maintenance overhead.

## Design Philosophy
- Builds on Jakarta Bean Validation
- No custom execution engine
- Focused on boundary validation (API payload validation)

## Compatibility

Validation Kit follows Jakarta Bean Validation standards and is tested with:

- Java 17+
- Spring Boot 3.x
- Hibernate Validator 8.x

The library does not modify the validation lifecycle and should remain compatible with future Jakarta Validation implementations.

## License

MIT
