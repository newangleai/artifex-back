package newangle.artifex.domain.user.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationDTO(
    @NotBlank(message = "username must not be blank")
    String username,

    @NotBlank(message = "password must not be blank")
    String password
) {
    
}