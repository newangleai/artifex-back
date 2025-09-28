package newangle.artifex.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import newangle.artifex.domain.user.User;
import newangle.artifex.domain.user.dto.UserResponseDTO;
import newangle.artifex.domain.user.dto.UserUpdateDTO;
import newangle.artifex.services.UserService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.access.prepost.PreAuthorize;


@RestController("/users")
public class UserController {
    
    @Autowired
    private UserService service;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<UserResponseDTO>> findAll() {
		List<User> list = service.findAll();
		List<UserResponseDTO> resp = list.stream().map(UserResponseDTO::from).collect(Collectors.toList());
		return ResponseEntity.ok().body(resp);
	}

    @GetMapping(value = "/{id}")
    @PreAuthorize("#id == principal.id or hasRole('ADMIN')")
	public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
		User obj = service.findById(id);
		return ResponseEntity.ok().body(UserResponseDTO.from(obj));
	}

    @PutMapping(value="/update-account/{id}")
    @PreAuthorize("#id == principal.id or hasRole('ADMIN')")
    public ResponseEntity<UserUpdateDTO> updateUserInfo(@PathVariable Long id, @RequestBody User user) {
        user = service.updateUser(id, user);
        return ResponseEntity.ok().body(UserUpdateDTO.from(user));
    }

    @DeleteMapping(value="/delete-account/{id}")
    @PreAuthorize("#id == principal.id or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}