# Validation Kit

A practical, modern validation library for Spring Boot applications.
Validation Kit provides high-value custom constraints like `@AllowedValues`, `@Base64`, and `@FileExtension` that are missing from the standard Jakarta Bean Validation spec.

## Features

- **`@AllowedValues`**: Validate a field against a fixed list of allowed strings.
    - Supports `String`, `Object` (toString), `Collection<?>`, and Arrays (`Object[]`).
    - Configurable case sensitivity (`caseSensitive=false`).
    - Configurable null handling (`acceptNull=false`).
- **`@Base64`**: Validate that a string is a valid Base64 encoded sequence.
- **`@FileExtension`**: Validate filenames against an allowed list of extensions (case-insensitive by default).
- **Structured JSON Errors**: Optional Spring Boot starter that provides a consistent, structured JSON response for validation errors.
- **Configurable**: Toggle error detail levels (unsafe values) via `application.yml`.

## Installation

Add the dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.validationkit</groupId>
    <artifactId>validation-spring-boot-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

## Quick Start

### 1. Define your DTO

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

## License

MIT
