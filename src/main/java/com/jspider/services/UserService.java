package com.jspider.services;

import org.springframework.stereotype.Service;

import com.jspider.DTO.UserDTO;
import com.jspider.Entity.*;
import com.jspider.Enums.*;
import com.jspider.Repository.UserRepository;
import com.jspider.responseStructure.ResponseStructure;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	// Register User
	public ResponseEntity<?> register(User user) {
		if (userRepository.findByEmail(user.getEmail()).isPresent()) {
			ResponseStructure<String> rs = new ResponseStructure<>();
			rs.setStatusCode(HttpStatus.BAD_REQUEST.value());
			rs.setMessage("Already Registered");
			rs.setData(user.getEmail());
			return new ResponseEntity<ResponseStructure<String>>(rs, HttpStatus.OK);
		}

		User usersave = userRepository.save(user);
		UserDTO dto = new UserDTO();
		BeanUtils.copyProperties(usersave, dto);
		ResponseStructure<UserDTO> rs = new ResponseStructure<>();
		rs.setStatusCode(HttpStatus.CREATED.value());
		rs.setMessage("Registered Successfully");
		rs.setData(dto);

		return new ResponseEntity<ResponseStructure<UserDTO>>(rs, HttpStatus.OK);
	}

	// Login User
	public ResponseEntity<?> login(String email, String password) {

		Optional<User> userOptional = userRepository.findByEmail(email);
		System.out.println("Email: " + email);
		System.out.println("Password: " + password);
		ResponseStructure<String> responseStructure = new ResponseStructure<>();

		// Check if user exists
		if (userOptional.isPresent()) {
			User user = userOptional.get();

			// Validate password
			if (user.getPassword().equals(password)) {
				responseStructure.setStatusCode(HttpStatus.OK.value());
				responseStructure.setMessage("Login Success");
				responseStructure.setData(email);
				return new ResponseEntity<>(responseStructure, HttpStatus.OK);
			} else {
				responseStructure.setStatusCode(HttpStatus.BAD_REQUEST.value());
				responseStructure.setMessage("Invalid Password");
				return new ResponseEntity<>(responseStructure, HttpStatus.BAD_REQUEST);
			}
		} else {
			// User not found
			responseStructure.setStatusCode(HttpStatus.NOT_FOUND.value());
			responseStructure.setMessage("User not found");
			return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);
		}

	}

	// Get User by ID (Only Active Users)
	public ResponseEntity<?> getUserById(Integer id) {

		Optional<User> user = userRepository.findById(id);
		if (user.isPresent()) {
			User userstatus = user.get();
			UserDTO userDTO = new UserDTO();
			BeanUtils.copyProperties(userstatus, userDTO);
			if (userstatus.getStatus() != Status.ACTIVE) {
				ResponseStructure<String> rs = new ResponseStructure<>();
				rs.setStatusCode(HttpStatus.BAD_REQUEST.value());
				rs.setMessage("Only active users can fetch details.");
//				rs.setData(userstatus.getStatus());
				return new ResponseEntity<ResponseStructure<String>>(rs, HttpStatus.OK);
			}
			ResponseStructure<UserDTO> rs = new ResponseStructure<>();
			rs.setStatusCode(HttpStatus.OK.value());
			rs.setMessage("Data Fetched !");
			rs.setData(userDTO);
			return new ResponseEntity<ResponseStructure<UserDTO>>(rs, HttpStatus.OK);
		} else {
			// User not found case
			ResponseStructure<String> rs = new ResponseStructure<>();
			rs.setStatusCode(HttpStatus.NOT_FOUND.value());
			rs.setMessage("User not found.");
			return new ResponseEntity<>(rs, HttpStatus.NOT_FOUND);
		}
	}

	// Admin: Get All Users
	public ResponseEntity<?> getAllUsers(Integer id) {

		Optional<User> user = userRepository.findById(id);
		if (!user.isPresent()) {
			ResponseStructure<String> rs = new ResponseStructure<>();
			rs.setStatusCode(HttpStatus.NOT_FOUND.value());
			rs.setMessage("User not found.");
			return new ResponseEntity<>(rs, HttpStatus.NOT_FOUND);
		}
		User getuser = user.get();
		if (getuser.getRole() != Role.ADMIN) {
			ResponseStructure<String> rs = new ResponseStructure<>();
			rs.setStatusCode(HttpStatus.BAD_REQUEST.value());
			rs.setMessage("Access Denied: Only admins can view all users..");
//			rs.setData(userstatus.getStatus());
			return new ResponseEntity<ResponseStructure<String>>(rs, HttpStatus.OK);

		} else {
			ResponseStructure<List<UserDTO>> rs = new ResponseStructure<>();
			List<User> allUsers = userRepository.findAll();

			List<UserDTO> userDTOList = allUsers.stream().map(allUser -> {
				UserDTO dto = new UserDTO();
				BeanUtils.copyProperties(allUser, dto);
				return dto;
			}).collect(Collectors.toList());
			rs.setStatusCode(HttpStatus.OK.value());
			rs.setMessage("Data Fetched !");
			rs.setData(userDTOList);
			return new ResponseEntity<>(rs, HttpStatus.OK);

		}

	}

	// Admin: Update User Status
	public ResponseEntity<?> updateStatus(Integer id, Integer adminid, Status status) {

		Optional<User> user = userRepository.findById(adminid);
		if (!user.isPresent()) {
			ResponseStructure<String> rs = new ResponseStructure<>();
			rs.setStatusCode(HttpStatus.NOT_FOUND.value());
			rs.setMessage("User not found.");
			return new ResponseEntity<>(rs, HttpStatus.NOT_FOUND);
		}
		User getuser = user.get();
		if (getuser.getRole() != Role.ADMIN) {
			ResponseStructure<String> rs = new ResponseStructure<>();
			rs.setMessage("Access Denied: Only admins can view all users.");
			rs.setStatusCode(HttpStatus.BAD_REQUEST.value());
			return new ResponseEntity<ResponseStructure<String>>(rs, HttpStatus.OK);
		}
		Optional<User> user1 = userRepository.findById(id);
		User targetUser = user1.get();
		targetUser.setStatus(status);
		userRepository.save(targetUser);
		ResponseStructure<UserDTO> rs = new ResponseStructure<>();
		rs.setStatusCode(HttpStatus.OK.value());
		rs.setMessage("User status updated successfully.");
		
		return new ResponseEntity<>(rs, HttpStatus.OK);
	}

	public ResponseEntity<?> updateScore(Integer studentId, Double score) {
		
		Optional<User> user1 = userRepository.findById(studentId);
		if (!user1.isPresent()) {
			ResponseStructure<String> rs = new ResponseStructure<>();
			rs.setStatusCode(HttpStatus.NOT_FOUND.value());
			rs.setMessage("User not found.");
			return new ResponseEntity<>(rs, HttpStatus.NOT_FOUND);
		}
		User targetUser = user1.get();
		targetUser.setUserTotalScore(score);
		userRepository.save(targetUser);
		UserDTO dto = new UserDTO();
		BeanUtils.copyProperties(targetUser, dto);
		ResponseStructure<UserDTO> rs = new ResponseStructure<>();
		rs.setStatusCode(HttpStatus.OK.value());
		rs.setMessage("User status updated successfully.");
		rs.setData(dto);
		return new ResponseEntity<>(rs, HttpStatus.OK);
	}
}
