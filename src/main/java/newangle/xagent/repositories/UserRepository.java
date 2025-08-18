package newangle.xagent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import newangle.xagent.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
}