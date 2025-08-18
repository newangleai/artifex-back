package newangle.xagent.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import newangle.xagent.entities.User;
import newangle.xagent.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;
    
    public User createUser(User user) {
        return repository.save(user);
    }

    public User updateUser(Long id, User u) {
        User user = repository.getReferenceById(id);
        updateUserInfo(user, u);
        return repository.save(user);
    }

    private void updateUserInfo(User user, User u) {
        user.setName(u.getName());
        user.setUsername(u.getUsername());
        user.setEmail(u.getEmail());
        user.setPassword(u.getPassword());
        user.setPhoneNumber(u.getPhoneNumber());
    }

}