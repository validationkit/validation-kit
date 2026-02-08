package io.github.validationkit.samples.controller;

import io.github.validationkit.constraints.AllowedValues;
import io.github.validationkit.samples.dto.UserRequest;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.validationkit.samples.dto.PasswordRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Validated // Required for method validation (@RequestParam)
public class UserController {

    @PostMapping
    public Map<String, String> createUser(@Valid @RequestBody UserRequest request) {
        return Map.of("status", "success", "message", "User created: " + request.getUsername());
    }

    @PostMapping("/validate-password")
    public Map<String, String> validatePassword(@Valid @RequestBody PasswordRequest request) {
        return Map.of("status", "success", "message", "Password is valid!");
    }

    @GetMapping("/search")
    public Map<String, String> searchUsers(
            @RequestParam @AllowedValues({ "active", "inactive" }) String status) {
        return Map.of("status", "success", "message", "Found users with status: " + status);
    }
}
