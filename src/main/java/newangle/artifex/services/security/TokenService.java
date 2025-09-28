package newangle.artifex.services.security;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import newangle.artifex.domain.user.User;

@Service
public class TokenService {

    @Value("${JWT_SECRET:${api.security.token.secret}}")
    private String secret;

    @Value("${api.security.token.expiration-hours:24}")
    private long expirationHours;

    @Value("${JWT_ISSUER:${api.security.token.issuer:x-agent}}")
    private String issuer;

    @Value("${JWT_AUDIENCE:${api.security.token.audience:xagent-users}}")
    private String audience;

    @Value("${JWT_LEEWAY_SECONDS:${api.security.token.leeway-seconds:60}}")
    private long leewaySeconds;
    
    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                            .withIssuer(issuer)
                            .withAudience(audience)
                            .withSubject(user.getUsername())
                            .withClaim("userId", user.getId())
                            .withExpiresAt(generateExpirationDate())
                            .sign(algorithm);
            return token;
        }
        catch (JWTCreationException e) {
            throw new RuntimeException("Error while generating token: ", e);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer(issuer)
                    .withAudience(audience)
                    .acceptLeeway(leewaySeconds)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            throw new RuntimeException("Error while verificating token: ", e);
        }
    }

    private Instant generateExpirationDate() {
        return Instant.now().plusSeconds(expirationHours * 3600);
    }

}