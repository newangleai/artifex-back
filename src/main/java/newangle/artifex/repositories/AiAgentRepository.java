package newangle.artifex.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import newangle.artifex.domain.agent.AiAgent;

public interface AiAgentRepository extends JpaRepository<AiAgent, UUID> {
    
}