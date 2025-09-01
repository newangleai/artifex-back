package newangle.xagent.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import newangle.xagent.domain.user.UserRole;
import newangle.xagent.domain.user.dto.RegisterDTO;
import newangle.xagent.domain.user.User;
import newangle.xagent.repositories.UserRepository;
import newangle.xagent.services.exceptions.ResourceNotFoundException;
import newangle.xagent.services.exceptions.UserAlreadyExistsException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
		Optional<User> obj = userRepository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(id));
	}

    public User createUser(RegisterDTO data) {
        if (userRepository.existsByUsername(data.username())) {
            throw new UserAlreadyExistsException("Conflict: Username '" + data.username() + "' already in use.");
        }
        if (userRepository.existsByEmail(data.email())) {
            throw new UserAlreadyExistsException("Conflict: Email '" + data.email() + "' already in use.");
        }

        String encodedPassword = passwordEncoder.encode(data.password());
        User user = new User(data.username(), encodedPassword, data.email(), data.phoneNumber(), UserRole.USER);

        User saved = userRepository.save(user);
        // Audit: user created (no sensitive data)
        log.info("audit.user.created userId={} username={} email={}", saved.getId(), saved.getUsername(), saved.getEmail());
        return saved;
    }

    public User updateUser(Long id, User u) {
        User user = userRepository.getReferenceById(id);
        String actor = getCurrentActor();
        String oldEmail = user.getEmail();
        String oldPhone = user.getPhoneNumber();
        boolean passwordWillChange = u.getPassword() != null && !u.getPassword().isBlank() && !passwordEncoder.matches(u.getPassword(), user.getPassword());

        updateUserInfo(user, u);
        User saved = userRepository.save(user);

        // Audit: user updated
        boolean emailChanged = oldEmail != null ? !oldEmail.equals(saved.getEmail()) : saved.getEmail() != null;
        boolean phoneChanged = oldPhone != null ? !oldPhone.equals(saved.getPhoneNumber()) : saved.getPhoneNumber() != null;
        log.info("audit.user.updated targetUserId={} actor={} emailChanged={} phoneChanged={} passwordChanged={}",
                saved.getId(), actor, emailChanged, phoneChanged, passwordWillChange);
        return saved;
    }

    private void updateUserInfo(User user, User u) {
        user.setEmail(u.getEmail());
        // Only update password if a new non-blank value is provided.
        // Re-hash the new password before saving. Avoid double-encoding by checking matches.
        if (u.getPassword() != null && !u.getPassword().isBlank()) {
            // If the provided raw password already matches the stored hash, do nothing.
            if (!passwordEncoder.matches(u.getPassword(), user.getPassword())) {
                user.setPassword(passwordEncoder.encode(u.getPassword()));
            }
        }
        user.setPhoneNumber(u.getPhoneNumber());
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
        // Audit: user deleted
        String actor = getCurrentActor();
        log.warn("audit.user.deleted targetUserId={} actor={}", id, actor);
    }

    private String getCurrentActor() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            return auth != null ? auth.getName() : "anonymous";
        } catch (Exception e) {
            return "unknown";
        }
    }

}