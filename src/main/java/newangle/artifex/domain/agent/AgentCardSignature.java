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
public class AgentCardSignature {
 
    // The protected JWS header for the signature
    private String protectedHeader;

    // The computed signature, Base64url-encoded
    private String signature;

    // The unprotected JWS header values
    private Map<String, Object> header;
 
}