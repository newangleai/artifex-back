package newangle.xagent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import newangle.xagent.domain.user.User;

public interface UserRepository extends JpaRepository<User, Long> {

    UserDetails findByUsername(String username);
    
}