package com.jspider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jspider.Entity.*;
import com.jspider.services.*;
import com.jspider.Enums.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserService userService;

	// Register a new user
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody User user) {
		return userService.register(user);
	}

	// Login a user
	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestParam String email, @RequestParam String password) {
		return userService.login(email, password);
	}

	// Get user by ID
	@GetMapping("/id/{id}")
	public ResponseEntity<?> getUserById(@PathVariable Integer id) {
		return userService.getUserById(id);
	}

	// Admin: Get all users
	@GetMapping("/all")
	public ResponseEntity<?> getAllUsers(@RequestParam Integer id) {
		return userService.getAllUsers(id);
	}

	// Admin: Update user status
	@PostMapping("/id/{id}/status")
	public ResponseEntity<?> updateUserStatus(@PathVariable Integer id, @RequestParam Integer adminid, @RequestParam Status status) {
		return userService.updateStatus(id, adminid, status);
	}
	@PutMapping("/saveTotalscore")
	public ResponseEntity<?>  UpdateTotalScore(@RequestParam Integer studentId,@RequestParam Double score) {
		//TODO: process POST request
		
		return userService.updateScore(studentId,score);
	}
	
}
