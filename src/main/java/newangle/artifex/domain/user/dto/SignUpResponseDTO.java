package newangle.artifex.domain.user.dto;

import newangle.artifex.domain.user.User;

public record SignUpResponseDTO(String username, String email, String phoneNumber) {

    public SignUpResponseDTO(User user) {
        this(user.getUsername(), user.getEmail(), user.getPhoneNumber());
    }
    
}