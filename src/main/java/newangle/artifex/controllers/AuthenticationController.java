package newangle.artifex.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import newangle.artifex.domain.user.User;
import newangle.artifex.domain.user.dto.AuthenticationDTO;
import newangle.artifex.domain.user.dto.RegisterDTO;
import newangle.artifex.domain.user.dto.SignInResponseDTO;
import newangle.artifex.domain.user.dto.SignUpResponseDTO;
import newangle.artifex.services.UserService;
import newangle.artifex.services.security.TokenService;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponseDTO> signUp(@RequestBody @Valid RegisterDTO data) {
        User newUser = userService.createUser(data);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newUser.getId()).toUri();

        SignUpResponseDTO signUpResponseDTO = new SignUpResponseDTO(newUser);

        return ResponseEntity.created(uri).body(signUpResponseDTO);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponseDTO> signIn(@RequestBody @Valid AuthenticationDTO data,
            HttpServletRequest request) {

        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new SignInResponseDTO(token));
    }

}