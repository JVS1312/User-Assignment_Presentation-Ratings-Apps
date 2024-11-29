package com.jspider.DTO;

import java.util.List;

import com.jspider.Entity.Presentation;
import com.jspider.Entity.Ratings;
import com.jspider.Enums.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	private Integer id;
	private String name;
	private String email;
	private Long phone;
	private Status status;
	private Double userTotalScore;

	private List<Presentation> presentations;
	private List<Ratings> ratings;
}
