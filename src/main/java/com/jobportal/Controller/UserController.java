package com.jobportal.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.User;
import com.jobportal.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	
	@Autowired
	private UserService userService;
	
	// POST - create a new user
	
	@PostMapping
	public ResponseEntity<User> createUser( @Valid @RequestBody User user) {
		User savedUser = userService.saveUser(user);
		return ResponseEntity.ok(savedUser);
	}
	// GET - Get user by ID
	@GetMapping("/{id}")
	public ResponseEntity<?> getUserById(@PathVariable Long id) {
		Optional<User> useropt = userService.getUserById(id);
		if(useropt.isPresent()) {
			return ResponseEntity.ok(useropt.get());
		} else {
			return ResponseEntity.status(404).body("user not found");
		}
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Long id) {
		boolean deleted = userService.deleteUserById(id);
		if(deleted) {
			return ResponseEntity.ok("User deleted Successfully");
		}
		else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
		}
	}
	@GetMapping
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> users = userService.getAllUsers();
		return ResponseEntity.ok(users);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody User updatedUser) {
		Optional<User> userOpt = userService.getUserById(id);
		if(userOpt.isPresent()) {
			User existingUser = userOpt.get();
			
			existingUser.setName(updatedUser.getName());
			existingUser.setEmail(updatedUser.getEmail());
			existingUser.setPassword(updatedUser.getPassword());
			existingUser.setMobile(updatedUser.getMobile());
			User saved = userService.saveUser(existingUser);
			
			return ResponseEntity.ok(saved);
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not found");
		}
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<?> patchUser(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
		Optional<User> userOpt = userService.getUserById(id);
		
		if(userOpt.isPresent()) {
			User user = userOpt.get();
			
			updates.forEach((key, value) -> {
				switch(key) {
				case "name" -> user.setName((String) value);
				case "email" -> user.setEmail((String) value);
				case "password" -> user.setPassword((String) value);
				case "mobile" -> user.setMobile((String) value);
				}
			});
			User savedUser = userService.saveUser(user);
			return ResponseEntity.ok(savedUser);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not found");
		}
	}
}