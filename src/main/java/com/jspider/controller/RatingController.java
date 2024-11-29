package com.jspider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.jspider.DTO.RatingDTO;
import com.jspider.Entity.*;
import com.jspider.responseStructure.ResponseStructure;
import com.jspider.services.*;

@RestController
@RequestMapping("/ratings")
public class RatingController {

	@Autowired
	private RatingService ratingService;

	// Admin: Rate a presentation
	@PostMapping("/rate")
	public ResponseEntity<ResponseStructure<RatingDTO>> ratePresentation(@RequestParam Integer studentId,
			@RequestParam Integer presentationId,
			@RequestBody Ratings rating, @RequestParam Integer adminid) {
		return ratingService.ratePresentation(studentId, presentationId, rating, adminid);
	}

	// Get rating by presentation ID
	@GetMapping("/presentation/{presentationId}")
	public ResponseEntity<ResponseStructure<RatingDTO>> getRatingByPresentationId(@PathVariable Integer presentationId) {
		return ratingService.getRatingByPresentationId(presentationId);
	}

	// Get all ratings for a student
	@GetMapping("/student/{studentId}")
	public ResponseEntity<ResponseStructure<List<RatingDTO>>> getAllRatingsByStudentId(@PathVariable Integer studentId) {
		return ratingService.getAllRatingsByStudentId(studentId);
	}
}
