package io.github.validationkit.samples.dto;

import io.github.validationkit.constraints.StrongPassword;

public class PasswordRequest {

    @StrongPassword(message = "Password is too weak")
    private String password;

    @StrongPassword(min = 12, hasSpecialChar = false, message = "Pin must be at least 12 chars, no special chars required")
    private String pin;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
