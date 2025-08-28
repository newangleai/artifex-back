package newangle.xagent.controllers;

import newangle.xagent.domain.user.User;
import newangle.xagent.domain.user.dto.AuthenticationDTO;
import newangle.xagent.domain.user.dto.RegisterDTO;
import newangle.xagent.domain.user.dto.SignInResponseDTO;
import newangle.xagent.domain.user.dto.SignUpResponseDTO;
import newangle.xagent.security.TokenService;
import newangle.xagent.services.UserService;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponseDTO> signUp(@RequestBody RegisterDTO data) {
        User newUser = userService.createUser(data);

        URI uri = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(newUser.getId()).toUri();

        SignUpResponseDTO signUpResponseDTO = new SignUpResponseDTO(newUser);

        return ResponseEntity.created(uri).body(signUpResponseDTO);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponseDTO> signIn(@RequestBody AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new SignInResponseDTO(token));
    }
    
}