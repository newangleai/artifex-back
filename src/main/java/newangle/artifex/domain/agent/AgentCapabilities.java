package newangle.artifex.domain.agent;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AgentCapabilities {
 
    // Indicates if the agent supports Server-Sent Events (SSE) for streaming responses
    private Boolean streaming;

    // Indicates if the agent supports sending push notifications for asynchronous task updates
    private Boolean pushNotifications;

    // Indicates if the agent provides a history of state transitions for a task
    private Boolean stateTransitionHistory;

    // A list of protocol extensions supported by the agent
    private AgentExtension extensions;

}