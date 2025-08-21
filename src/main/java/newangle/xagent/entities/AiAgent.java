package newangle.xagent.entities;

import java.time.Instant;
import java.util.UUID;

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

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ai_agents")
public class AiAgent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String externalId;
    private String name;
    private String description;
    private Instant createdAt;
    
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