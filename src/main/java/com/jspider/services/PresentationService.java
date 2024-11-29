package com.jspider.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jspider.DTO.PresentationDTO;
import com.jspider.Entity.*;
import com.jspider.Enums.*;
import com.jspider.Repository.*;
import com.jspider.responseStructure.ResponseStructure;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PresentationService {

	@Autowired
	private PresentationRepository presentationRepository;

	@Autowired
	private UserRepository userRepository;

	// Admin: Assign Presentation to a Student
	public ResponseEntity<ResponseStructure<PresentationDTO>> assignPresentation(Integer studentId,
			Presentation presentation, Integer adminId) {
		ResponseStructure<PresentationDTO> response = new ResponseStructure<>();

		// Verify admin user
		Optional<User> adminOptional = userRepository.findById(adminId);
		if (!adminOptional.isPresent()) {
			response.setStatusCode(HttpStatus.NOT_FOUND.value());
			response.setMessage("Admin not found.");
			response.setData(null);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		User adminUser = adminOptional.get();
		if (adminUser.getRole() != Role.ADMIN) {
			response.setStatusCode(HttpStatus.FORBIDDEN.value());
			response.setMessage("Access Denied: Only admins can assign presentations.");
			response.setData(null);
			return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
		}

		// Find student user
		Optional<User> studentOptional = userRepository.findById(studentId);
		if (!studentOptional.isPresent()) {
			response.setStatusCode(HttpStatus.NOT_FOUND.value());
			response.setMessage("Student not found.");
			response.setData(null);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		User studentUser = studentOptional.get();
		presentation.setUser(studentUser);

		
        studentUser.setUserTotalScore(presentation.getUserTotalScore()+studentUser.getUserTotalScore()); 
		Presentation savedPresentation = presentationRepository.save(presentation);
		@SuppressWarnings("unused")
		User saveStudent= userRepository.save(studentUser);
		PresentationDTO dto = new PresentationDTO();
		BeanUtils.copyProperties(savedPresentation, dto);

		response.setStatusCode(HttpStatus.CREATED.value());
		response.setMessage("Presentation assigned successfully.");
		response.setData(dto);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	// Get Presentation by ID
	public ResponseEntity<ResponseStructure<PresentationDTO>> getPresentationById(Integer id) {

		ResponseStructure<PresentationDTO> response = new ResponseStructure<>();

		Presentation presentation = presentationRepository.findById(id).orElse(null);
		if (presentation == null) {
			response.setStatusCode(HttpStatus.NOT_FOUND.value());
			response.setMessage("Presentation not found.");
			return new ResponseEntity<ResponseStructure<PresentationDTO>>(response, HttpStatus.NOT_FOUND);
		}

		PresentationDTO dto = new PresentationDTO();
		BeanUtils.copyProperties(presentation, dto);

		response.setStatusCode(HttpStatus.OK.value());
		response.setMessage("Presentation fetched successfully.");
		response.setData(dto);

		return new ResponseEntity<ResponseStructure<PresentationDTO>>(response, HttpStatus.OK);
	}

	// Get All Presentations by Student ID
	public ResponseEntity<ResponseStructure<List<PresentationDTO>>> getAllPresentationsByStudentId(Integer studentId) {
		ResponseStructure<List<PresentationDTO>> response = new ResponseStructure<>();

		List<Presentation> presentations = presentationRepository.findByUserId(studentId);

		List<PresentationDTO> dtoList = presentations.stream().map(presentation -> {
			PresentationDTO dto = new PresentationDTO();
			BeanUtils.copyProperties(presentation, dto);
			return dto;
		}).collect(Collectors.toList());

		response.setStatusCode(HttpStatus.OK.value());
		response.setMessage("Presentations fetched successfully.");
		response.setData(dtoList);

		return new ResponseEntity<ResponseStructure<List<PresentationDTO>>>(response, HttpStatus.OK);
	}

	// admin: Change Presentation Status
	public ResponseEntity<ResponseStructure<PresentationDTO>> changePresentationStatus(Integer id,
			PresentationStatus status, Integer adminId) {
		ResponseStructure<PresentationDTO> response = new ResponseStructure<>();

		User adminUser = userRepository.findById(adminId).orElse(null);
		if (adminUser == null)
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

		Presentation presentation = presentationRepository.findById(id).orElse(null);
		if (presentation == null) {
			response.setStatusCode(HttpStatus.NOT_FOUND.value());
			response.setMessage("Presentation not found.");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		presentation.setPresentationStatus(status);
		Presentation updatedPresentation = presentationRepository.save(presentation);

		PresentationDTO dto = new PresentationDTO();
		BeanUtils.copyProperties(updatedPresentation, dto);

		response.setStatusCode(HttpStatus.OK.value());
		response.setMessage("Presentation status updated successfully.");
		response.setData(dto);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// Admin: Save Total Score of a Presentation
	public ResponseEntity<ResponseStructure<PresentationDTO>> saveTotalScore(Integer pid, Double score,
			Integer adminid) {
		Optional<User> user = userRepository.findById(adminid);
		if (!user.isPresent()) {
			ResponseStructure<PresentationDTO> response = new ResponseStructure<>();
			response.setStatusCode(HttpStatus.NOT_FOUND.value());
			response.setMessage("admin not found.");
			return new ResponseEntity<ResponseStructure<PresentationDTO>>(response, HttpStatus.NOT_FOUND);
		}
		User getuser = user.get();
		if (getuser.getRole() != Role.ADMIN) {
			throw new RuntimeException("Access Denied: Only admins can edit ststus of- users.");

		}
		ResponseStructure<PresentationDTO> response = new ResponseStructure<>();
		Presentation presentation = presentationRepository.findById(pid).orElse(null);
		if (presentation == null) {
			response.setStatusCode(HttpStatus.NOT_FOUND.value());
			response.setMessage("Presentation not found.");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		 User studentUser = presentation.getUser();
         studentUser.setUserTotalScore(score+studentUser.getUserTotalScore()); 
         presentation.setUserTotalScore(score); 

         Presentation updatedPresentation = presentationRepository.save(presentation); 

         PresentationDTO dto = new PresentationDTO();
         BeanUtils.copyProperties(updatedPresentation, dto);

         response.setStatusCode(HttpStatus.OK.value());
         response.setMessage("Total score saved successfully.");
         response.setData(dto);
         return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
