package newangle.artifex.domain.agent;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AgentExtension {
 
    // The unique URI identifying the extension
    private String uri;

    // A human-readable description of how this agent uses the extension
    private String description;

    // If true, the client must understand and comply with the extension's requirements to interact with the agent
    private Boolean required;

    // Optional, extension-specific configuration parameters
    private Map<String, Object> params;

}