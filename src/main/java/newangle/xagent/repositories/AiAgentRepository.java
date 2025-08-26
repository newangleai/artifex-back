package newangle.xagent.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import newangle.xagent.domain.agent.AiAgent;

public interface AiAgentRepository extends JpaRepository<AiAgent, UUID> {
    
}