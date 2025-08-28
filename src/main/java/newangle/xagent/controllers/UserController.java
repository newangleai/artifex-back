package newangle.xagent.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import newangle.xagent.domain.user.User;
import newangle.xagent.domain.user.dto.UserResponseDTO;
import newangle.xagent.services.UserService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * 
 */
@RestController
public class UserController {
    
    @Autowired
    private UserService service;

    @GetMapping(value="/users")
	public ResponseEntity<List<UserResponseDTO>> findAll() {
		List<User> list = service.findAll();
		List<UserResponseDTO> resp = list.stream().map(UserResponseDTO::from).collect(Collectors.toList());
		return ResponseEntity.ok().body(resp);
	}

    @GetMapping(value = "/users/{id}")
	public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
		User obj = service.findById(id);
		return ResponseEntity.ok().body(UserResponseDTO.from(obj));
	}

    @PutMapping(value="/users/update-account/{id}")
    @PreAuthorize("#id == principal.id or hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> updateUserInfo(@PathVariable Long id, @RequestBody User user) {
        user = service.updateUser(id, user);
        return ResponseEntity.ok().body(UserResponseDTO.from(user));
    }

    @DeleteMapping(value="/users/delete-account/{id}")
    @PreAuthorize("#id == principal.id or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}