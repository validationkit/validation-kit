package io.github.validationkit.samples;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ValidationIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @SuppressWarnings("null")
  @Test
  void shouldReturn200ForValidRequest() throws Exception {
    String validJson = """
        {
          "username": "coder123",
          "role": "admin",
          "profileImageName": "pic.png",
          "permissions": ["developer"],
          "tags": ["java", "spring"]
        }
        """;

    mockMvc.perform(post("/api/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(validJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("User created: coder123"));
  }

  @SuppressWarnings("null")
  @Test
  void shouldReturn400ForInvalidRequest() throws Exception {
    String invalidJson = """
        {
          "username": "",
          "role": "super-admin",
          "profileImageName": "pic.exe",
          "permissions": ["hacker"],
          "tags": ["cobol"]
        }
        """;

    mockMvc.perform(post("/api/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(invalidJson))
        .andExpect(status().isBadRequest())
        // Verify global exception handler structure
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.message").value("Validation failed"))
        .andExpect(jsonPath("$.errors", hasSize(greaterThan(0))))

        // Verify specific field errors
        // Role: AllowedValues
        .andExpect(jsonPath("$.errors[?(@.field == 'role')].code").value(hasItem("AllowedValues")))
        // ProfileImage: FileExtension
        .andExpect(jsonPath("$.errors[?(@.field == 'profileImageName')].code").value(hasItem("FileExtension")))
        // Permissions: AllowedValues (collection)
        .andExpect(jsonPath("$.errors[?(@.field == 'permissions')].code").value(hasItem("AllowedValues")))
        // Tags: AllowedValues (array)
        .andExpect(jsonPath("$.errors[?(@.field == 'tags')].code").value(hasItem("AllowedValues")));
  }

  @SuppressWarnings("null")
  @Test
  void shouldReturn400ForInvalidBase64() throws Exception {
    String invalidJson = """
        {
          "username": "coder123",
          "role": "user",
          "avatarBase64": "this-is-not-base64!",
          "profileImageName": "pic.png",
          "permissions": ["developer"],
          "tags": ["java"]
        }
        """;

    mockMvc.perform(post("/api/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(invalidJson))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[?(@.field == 'avatarBase64')].code").value(hasItem("Base64")))
        .andExpect(jsonPath("$.errors[?(@.field == 'avatarBase64')].message")
            .value(hasItem("Avatar must be a valid Base64 string")));
  }
}
