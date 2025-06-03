package com.jobportal;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	// Save new User
	public User saveUser(User user) {
		return userRepository.save(user);
	}
	// Get user by ID
	public Optional<User> getUserById(Long id) {
		return userRepository.findById(id);
	}
	//Get all Users
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}
	//Delete user by Id
	public boolean deleteUserById(Long id) {
		   Optional<User> userOpt = userRepository.findById(id);
		   if(userOpt.isPresent()) {
			   userRepository.deleteById(id);
			   return true;
		   }
		   return false;
	}
	// find by email(for login or validations)
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}	
	
}