package newangle.xagent.domain.user.dto;

import java.util.Set;

import newangle.xagent.domain.agent.AiAgent;
import newangle.xagent.domain.user.User;

public record UserResponseDTO(String username, String password, String email, String phoneNumber, Set<AiAgent> aiAgents) {

    public static UserResponseDTO from (User user) {
        return new UserResponseDTO(
            user.getUsername(),
            user.getPassword(),
            user.getEmail(),
            user.getPhoneNumber(),
            user.getAiAgents()
        );
    }
    
}