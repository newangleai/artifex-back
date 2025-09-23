package newangle.xagent.domain.agent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AgentInterface {

    // The URL where this interface is available
    private String url;
    
    // The transport protocol supported at this URL
    private TransportProtocol transport;
    
}