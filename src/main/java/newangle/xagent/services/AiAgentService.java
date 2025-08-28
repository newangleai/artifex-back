package newangle.xagent.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import newangle.xagent.repositories.AiAgentRepository;
import newangle.xagent.repositories.UserRepository;
import newangle.xagent.services.exceptions.ResourceNotFoundException;
import newangle.xagent.domain.agent.AiAgent;
import newangle.xagent.domain.user.User;

@Service
public class AiAgentService {
    
    @Autowired
    private AiAgentRepository aiAgentrepository;

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
        return aiAgentrepository.save(aiAgent);
    }

    public AiAgent findById(UUID id) {
        Optional<AiAgent> obj = aiAgentrepository.findById(id);
        return obj.orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public void deleteAgent(UUID id) {
        aiAgentrepository.deleteById(id);
    }

    public AiAgent updateAgent(UUID id, AiAgent aiAgent) {
        AiAgent agent = aiAgentrepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        updateAgentInfo(agent, aiAgent);
        return aiAgentrepository.save(agent);
    }

    private void updateAgentInfo(AiAgent agent, AiAgent aiAgent) {
        agent.setName(aiAgent.getName());
        agent.setDescription(aiAgent.getDescription());
    }

}