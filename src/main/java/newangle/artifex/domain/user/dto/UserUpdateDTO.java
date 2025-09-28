package newangle.artifex.domain.user.dto;

import newangle.artifex.domain.user.User;

public record UserUpdateDTO(String username, String email, String phoneNumber, String password) {

    public static UserUpdateDTO from (User user) {
        return new UserUpdateDTO(
            user.getUsername(),
            user.getPassword(),
            user.getEmail(),
            user.getPhoneNumber()
        );
    }
    
}