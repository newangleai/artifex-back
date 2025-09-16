package newangle.xagent.domain.agent;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import newangle.xagent.domain.user.User;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ai_agents")
public class AiAgent {

    // === A2A COMPLIANCE ===
    private String protocolVersion;
    private String name;
    private String description;
    private String url;
    private TransportProtocol transportProtocol;
    private AgentInterface agentInterface;
    private String iconUrl;
    private AgentProvider provider;
    private String version;
    private String documentationUrl;
    private AgentCapabilities agentCapabilities;
    // private Map<String, SecurityScheme> securitySchemes; -> A declaration of the security schemes available to authorize requests. The key is the scheme name. Follows the OpenAPI 3.0 Security Scheme Object
    private List<Map<String, List<String>>> security;
    private String[] defaultInputModes;
    private String[] defaultOutputModes;
    private AgentSkill agentSkill;
    private Boolean supportsAuthenticatedExtendedCard;
    private AgentCardSignature signatures;
    

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Instant createdAt;
    
    @JsonBackReference
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    private void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
    }
    
}