package newangle.artifex.domain.agent;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AgentSkill {
 
    private String id;
    private String name;
    private String description;
    private String[] tags;
    private String[] examples;
    private String[] outputModes;

    // Security schemes necessary for the agent to leverage this skill
    private List<Map<String, List<String>>> security;

}