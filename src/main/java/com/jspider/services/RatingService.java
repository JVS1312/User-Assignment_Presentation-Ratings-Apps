package com.jspider.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jspider.DTO.RatingDTO;
import com.jspider.Entity.*;
import com.jspider.Enums.*;
import com.jspider.Repository.*;
import com.jspider.responseStructure.ResponseStructure;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RatingService {

	@Autowired
	private RatingRepository ratingRepository;

	@Autowired
	private PresentationRepository presentationRepository;

	@Autowired
	private UserRepository userRepository;

	// Admin: Rate a Presentation
	public ResponseEntity<ResponseStructure<RatingDTO>> ratePresentation(Integer studentId, Integer presentationId,
			Ratings rating, Integer adminId) {
		ResponseStructure<RatingDTO> response = new ResponseStructure<>();

		Optional<User> userOptional = userRepository.findById(adminId);
		if (!userOptional.isPresent()) {
			response.setStatusCode(HttpStatus.NOT_FOUND.value());
			response.setMessage("User not found with ID: " + adminId);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		User adminUser = userOptional.get();
		if (adminUser.getRole() != Role.ADMIN) {
			response.setStatusCode(HttpStatus.FORBIDDEN.value());
			response.setMessage("Access Denied: Only admins can rate presentations.");
			return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
		}

		User student = userRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found."));

		Presentation presentation = presentationRepository.findById(presentationId)
				.orElseThrow(() -> new RuntimeException("Presentation not found."));

		rating.setUser(student);
		
		rating.setPresentation(presentation);
		presentation.setUserTotalScore(rating.getTotalScore());
		student.setUserTotalScore(student.getUserTotalScore()+rating.getTotalScore());
		Ratings savedRating = ratingRepository.save(rating);
		presentationRepository.save(presentation);
		userRepository.save(student);
		RatingDTO dto = new RatingDTO();
		BeanUtils.copyProperties(savedRating, dto);

		response.setStatusCode(HttpStatus.CREATED.value());
		response.setMessage("Presentation rated successfully.");
		response.setData(dto);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	// Get Rating for a Particular Presentation
	public ResponseEntity<ResponseStructure<RatingDTO>> getRatingByPresentationId(Integer presentationId) {
		ResponseStructure<RatingDTO> response = new ResponseStructure<>();

		Ratings rating = ratingRepository.findByPresentationPid(presentationId)
				.orElseThrow(() -> new RuntimeException("Rating not found."));

		RatingDTO dto = new RatingDTO();
		BeanUtils.copyProperties(rating, dto);

		response.setStatusCode(HttpStatus.OK.value());
		response.setMessage("Rating fetched successfully.");
		response.setData(dto);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// Get All Ratings for a Student
	public ResponseEntity<ResponseStructure<List<RatingDTO>>> getAllRatingsByStudentId(Integer studentId) {
		ResponseStructure<List<RatingDTO>> response = new ResponseStructure<>();

		List<Ratings> ratings = ratingRepository.findByUserId(studentId);

		List<RatingDTO> dtoList = ratings.stream().map(rating -> {
			RatingDTO dto = new RatingDTO();
			BeanUtils.copyProperties(rating, dto);
			return dto;
		}).collect(Collectors.toList());

		response.setStatusCode(HttpStatus.OK.value());
		response.setMessage("Ratings fetched successfully.");
		response.setData(dtoList);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
