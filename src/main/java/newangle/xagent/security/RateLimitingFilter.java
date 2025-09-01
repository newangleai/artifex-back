package newangle.xagent.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import newangle.xagent.services.exceptions.TooManyRequestsException;
import newangle.xagent.services.security.LoginAttemptService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private final LoginAttemptService loginAttemptService;
    
    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;

    public RateLimitingFilter(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        String ip = resolveClientIp(request);

        // Apply per-IP request rate limiting for sign-in endpoint
        if ("/sign-in".equals(path) && "POST".equalsIgnoreCase(request.getMethod())) {
            // Sliding window increment
            loginAttemptService.onRequest(ip);

            if (loginAttemptService.isIpBlocked(ip) || loginAttemptService.isIpRateLimited(ip)) {
                handlerExceptionResolver.resolveException(
                    request,
                    response,
                    null,
                    new TooManyRequestsException("We were unable to process your request at this time. Try again later.")
                );
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        // Only filter login endpoint
        return !("/sign-in".equals(request.getRequestURI()) && "POST".equalsIgnoreCase(request.getMethod()));
    }

    private String resolveClientIp(HttpServletRequest request) {
        String xf = request.getHeader("X-Forwarded-For");
        if (xf != null && !xf.isBlank()) {
            return xf.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
