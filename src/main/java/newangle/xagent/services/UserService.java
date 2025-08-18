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

}