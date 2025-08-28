package newangle.xagent.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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