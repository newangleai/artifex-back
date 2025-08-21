package newangle.xagent.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import newangle.xagent.entities.AiAgent;

public interface AiAgentRepository extends JpaRepository<AiAgent, UUID> {
    
}