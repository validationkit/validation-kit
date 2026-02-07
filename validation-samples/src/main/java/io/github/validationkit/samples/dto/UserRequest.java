package io.github.validationkit.samples.dto;

import io.github.validationkit.constraints.AllowedValues;
import io.github.validationkit.constraints.Base64;
import io.github.validationkit.constraints.FileExtension;
import jakarta.validation.constraints.NotBlank;
import java.util.Collection;

public class UserRequest {

    @NotBlank(message = "Username is required")
    private String username;

    @AllowedValues(value = { "admin", "user", "guest" }, message = "Role must be one of {value}")
    @NotBlank(message = "Role is required")
    private String role;

    @Base64(message = "Avatar must be a valid Base64 string")
    private String avatarBase64;

    @FileExtension(value = { "jpg", "png" }, message = "Profile image must be strict jpg/png")
    private String profileImageName;

    @AllowedValues(value = { "developer", "manager", "tester" }, message = "Permissions must be valid")
    private Collection<String> permissions;

    @AllowedValues(value = { "java", "spring", "react" }, message = "Tags must be valid technology stacks")
    private String[] tags;

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAvatarBase64() {
        return avatarBase64;
    }

    public void setAvatarBase64(String avatarBase64) {
        this.avatarBase64 = avatarBase64;
    }

    public String getProfileImageName() {
        return profileImageName;
    }

    public void setProfileImageName(String profileImageName) {
        this.profileImageName = profileImageName;
    }

    public Collection<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Collection<String> permissions) {
        this.permissions = permissions;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }
}
