package com.entertainment.filmflix.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
    private Long userId;

    @NotBlank(message = "User name is required")
    @Size(min = 12, max=100, message = "User given name must be at least 12 characters and a maximum of 100 characters")
    private String givenName;

    @NotBlank(message = "Email ID is required")
    @Email(message = "Invalid email format")
    @Size(min = 12, max=100, message = "Email ID must be at least 12 characters and a maximum of 100 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max=100, message = "Password must be at least 6 characters and a maximum of 100 characters")
    private String password;
}
