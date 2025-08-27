package newangle.xagent.controllers;

import newangle.xagent.domain.user.AuthenticationDTO;
import newangle.xagent.domain.user.RegisterDTO;
import newangle.xagent.domain.user.User;
import newangle.xagent.repositories.UserRepository;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/sign-in")
    public ResponseEntity signIn(@RequestBody AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/sign-up")
    public ResponseEntity<User> signUp(@RequestBody RegisterDTO data) {
        if (this.userRepository.findByUsername(data.username()) != null) return ResponseEntity.badRequest().build();

        String encyptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User user = new User(data.username(), encyptedPassword, data.role());

        this.userRepository.save(user);

        return ResponseEntity.ok().build();
    }
    
}