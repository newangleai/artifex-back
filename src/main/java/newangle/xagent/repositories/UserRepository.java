package newangle.xagent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import newangle.xagent.domain.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
}