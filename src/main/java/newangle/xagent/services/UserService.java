package newangle.xagent.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import newangle.xagent.domain.user.UserRole;
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

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
		Optional<User> obj = userRepository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(id));
	}
    
    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User createUser(String username, String rawPassword, String email, String phoneNumber) {
        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException("Conflict: Username '" + username + "' already in use.");
        }
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("Conflict: Email '" + email + "' already in use.");
        }

        String encodedPassword = passwordEncoder.encode(rawPassword);
        User user = new User(username, encodedPassword, email, phoneNumber, UserRole.USER);

        return userRepository.save(user);
    }

    public User updateUser(Long id, User u) {
        User user = userRepository.getReferenceById(id);
        updateUserInfo(user, u);
        return userRepository.save(user);
    }

    private void updateUserInfo(User user, User u) {
        user.setEmail(u.getEmail());
        user.setPassword(u.getPassword());
        user.setPhoneNumber(u.getPhoneNumber());
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

}