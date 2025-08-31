package newangle.xagent.security;

import jakarta.servlet.http.HttpServletRequest;
import newangle.xagent.services.security.LoginAttemptService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class AuthenticationEventsListener implements ApplicationListener<AbstractAuthenticationEvent> {

    private final LoginAttemptService loginAttemptService;
    private static final Logger log = LoggerFactory.getLogger(AuthenticationEventsListener.class);

    public AuthenticationEventsListener(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public void onApplicationEvent(@NonNull AbstractAuthenticationEvent event) {
        if (event instanceof AuthenticationSuccessEvent success) {
            Authentication auth = success.getAuthentication();
            String username = auth.getName();
            String ip = getClientIp();
            loginAttemptService.onAuthenticationSuccess(username, ip);
            // Audit log: successful login (do not log credentials or tokens)
            log.info("auth.success username={} ip={} type=LOGIN_SUCCESS", username, ip);
        } else if (event instanceof AbstractAuthenticationFailureEvent failure) {
            Authentication auth = failure.getAuthentication();
            String username = auth != null ? auth.getName() : null;
            String ip = getClientIp();
            loginAttemptService.onAuthenticationFailure(username, ip);
            // Audit log: failed login
            log.warn("auth.failure username={} ip={} type=LOGIN_FAILURE reason={}", username, ip, failure.getException().getClass().getSimpleName());
        }
    }

    private String getClientIp() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) return null;
        HttpServletRequest request = attrs.getRequest();
        String xf = request.getHeader("X-Forwarded-For");
        if (xf != null && !xf.isBlank()) {
            return xf.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
