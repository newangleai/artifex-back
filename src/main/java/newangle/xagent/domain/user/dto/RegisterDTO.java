package newangle.xagent.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterDTO(
    @NotBlank(message = "username is a required field")
    String username,

    @NotBlank(message = "email is a required field")
    @Email(message = "email must be valid")
    String email,

    @NotBlank(message = "phone number is a required field")
    @Pattern(regexp = "^[0-9]{8,9}$", message = "phone number must have 8 or 9 digits")
    String phoneNumber,

    @NotBlank(message = "password is a required field")
    @Size(min = 8, message = "password must be at least 8 characters")
    String password
) {
    
}