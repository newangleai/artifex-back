package newangle.xagent.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import newangle.xagent.repositories.AiAgentRepository;
import newangle.xagent.repositories.UserRepository;
import newangle.xagent.entities.AiAgent;
import newangle.xagent.entities.User;

@Service
public class AiAgentService {
    
    @Autowired
    private AiAgentRepository repository;

    @Autowired
    private UserRepository userRepository;

    public AiAgent create(AiAgent aiAgent) {
        if (aiAgent == null || aiAgent.getUser() == null || aiAgent.getUser().getId() == null) {
            throw new IllegalArgumentException("AI Agent must be associated with a user");
        }

        Long userId = aiAgent.getUser().getId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        aiAgent.setUser(user);
        return repository.save(aiAgent);
    }

}