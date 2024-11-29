package com.jspider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.jspider.DTO.PresentationDTO;
import com.jspider.Entity.*;
import com.jspider.services.*;
import com.jspider.Enums.*;
import com.jspider.responseStructure.ResponseStructure;

@RestController
@RequestMapping("/presentations")
public class PresentationController {
	@Autowired
	private PresentationService presentationService;

	// Admin: Assign a presentation to a student
	@PostMapping("/assign")
	public ResponseEntity<?> assignPresentation(@RequestParam Integer studentId, @RequestBody Presentation presentation,
			@RequestParam Integer adminid) {
		return presentationService.assignPresentation(studentId, presentation, adminid);
	}

	// Get presentation by ID
	@GetMapping("pid/{id}")
	public ResponseEntity<ResponseStructure<PresentationDTO>> getPresentationById(@PathVariable Integer id) {
		return presentationService.getPresentationById(id);
	}

	// Get all presentations by student ID
	@GetMapping("/student/{studentId}")
	public ResponseEntity<ResponseStructure<List<PresentationDTO>>> getAllPresentationsByStudentId(@PathVariable Integer studentId) {
		return presentationService.getAllPresentationsByStudentId(studentId);
	}

	// Student: Change presentation status
	@PostMapping("/{id}/status")
	public ResponseEntity<ResponseStructure<PresentationDTO>> changePresentationStatus(@PathVariable Integer id, @RequestParam PresentationStatus status,
			@RequestParam Integer adminId) {
		return presentationService.changePresentationStatus(id, status, adminId);
	}

	// Admin: Save total score for a presentation
	@PostMapping("/{pid}/score")
	public ResponseEntity<ResponseStructure<PresentationDTO>> saveTotalScore(@PathVariable Integer pid, @RequestParam Double score, @RequestParam Integer adminId) {
		return presentationService.saveTotalScore(pid, score, adminId);
	}
}
