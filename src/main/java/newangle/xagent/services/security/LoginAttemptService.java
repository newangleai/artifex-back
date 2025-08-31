package newangle.xagent.services.security;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

@Service
public class LoginAttemptService {

    private static final int MAX_FAILED_ATTEMPTS = 5; // lock after 5 failures
    private static final Duration LOCK_DURATION = Duration.ofMinutes(15);

    private static final int MAX_REQUESTS_PER_MINUTE = 10; // rate limit for /sign-in per IP
    private static final Duration RATE_WINDOW = Duration.ofMinutes(1);

    private final ConcurrentHashMap<String, AttemptState> byIp = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AttemptState> byUsername = new ConcurrentHashMap<>();

    public boolean isIpBlocked(String ip) {
        AttemptState state = byIp.get(ip);
        return state != null && state.isLocked();
    }

    public boolean isUsernameBlocked(String username) {
        AttemptState state = byUsername.get(username);
        return state != null && state.isLocked();
    }

    public Instant ipBlockedUntil(String ip) {
        AttemptState state = byIp.get(ip);
        return state != null ? state.lockedUntil : null;
    }

    public Instant usernameBlockedUntil(String username) {
        AttemptState state = byUsername.get(username);
        return state != null ? state.lockedUntil : null;
    }

    public boolean isIpRateLimited(String ip) {
        AttemptState state = byIp.computeIfAbsent(ip, k -> new AttemptState());
        state.refillIfWindowElapsed(RATE_WINDOW);
        return state.requestsInWindow.get() >= MAX_REQUESTS_PER_MINUTE;
    }

    public void onAuthenticationFailure(String username, String ip) {
        if (ip != null) {
            AttemptState s = byIp.computeIfAbsent(ip, k -> new AttemptState());
            s.failedAttempts.incrementAndGet();
            s.requestsInWindow.incrementAndGet();
            if (s.failedAttempts.get() >= MAX_FAILED_ATTEMPTS) {
                s.lock(LOCK_DURATION);
            }
        }
        if (username != null) {
            AttemptState s = byUsername.computeIfAbsent(username, k -> new AttemptState());
            s.failedAttempts.incrementAndGet();
            if (s.failedAttempts.get() >= MAX_FAILED_ATTEMPTS) {
                s.lock(LOCK_DURATION);
            }
        }
    }

    public void onAuthenticationSuccess(String username, String ip) {
        if (username != null) {
            AttemptState s = byUsername.get(username);
            if (s != null) s.resetFailures();
        }
        if (ip != null) {
            AttemptState s = byIp.get(ip);
            if (s != null) s.resetFailures();
        }
    }

    public void onRequest(String ip) {
        if (ip == null) return;
        AttemptState s = byIp.computeIfAbsent(ip, k -> new AttemptState());
        s.refillIfWindowElapsed(RATE_WINDOW);
        s.requestsInWindow.incrementAndGet();
    }

    private static class AttemptState {
        private final AtomicInteger failedAttempts = new AtomicInteger(0);
        private final AtomicInteger requestsInWindow = new AtomicInteger(0);
        private Instant windowStart = Instant.now();
        private Instant lockedUntil = null;

        void lock(Duration duration) {
            this.lockedUntil = Instant.now().plus(duration);
        }

        boolean isLocked() {
            if (lockedUntil == null) return false;
            if (Instant.now().isAfter(lockedUntil)) {
                // auto-unlock
                lockedUntil = null;
                failedAttempts.set(0);
                return false;
            }
            return true;
        }

        void resetFailures() {
            failedAttempts.set(0);
            if (lockedUntil != null && Instant.now().isAfter(lockedUntil)) {
                lockedUntil = null;
            }
        }

        void refillIfWindowElapsed(Duration window) {
            if (Instant.now().isAfter(windowStart.plus(window))) {
                windowStart = Instant.now();
                requestsInWindow.set(0);
            }
        }

        @Override
        public int hashCode() {
            return Objects.hash(windowStart, lockedUntil);
        }
    }
}
