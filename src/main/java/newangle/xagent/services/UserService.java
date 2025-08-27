package newangle.xagent.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import newangle.xagent.domain.user.UserRole;
import newangle.xagent.domain.user.User;
import newangle.xagent.repositories.UserRepository;
import newangle.xagent.services.exceptions.ResourceNotFound;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return repository.findAll();
    }

    public User findById(Long id) {
		Optional<User> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFound(id));
	}
    
    public User createUser(User user) {
        return repository.save(user);
    }

    public User createUser(String username, String rawPassword) {
        String encodedPassword = passwordEncoder.encode(rawPassword);
        User user = new User(username, encodedPassword, UserRole.USER);
        return repository.save(user);
    }

    public User updateUser(Long id, User u) {
        User user = repository.getReferenceById(id);
        updateUserInfo(user, u);
        return repository.save(user);
    }

    private void updateUserInfo(User user, User u) {
        user.setEmail(u.getEmail());
        user.setPassword(u.getPassword());
        user.setPhoneNumber(u.getPhoneNumber());
    }

    public void deleteUser(Long id) {
        repository.deleteById(id);
    }

}