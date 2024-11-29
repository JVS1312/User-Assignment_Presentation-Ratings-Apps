package com.jspider.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jspider.Entity.Presentation;
import com.jspider.Entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RatingDTO {
	private Integer rid;

	private Double totalScore;
	@JsonIgnore
	private User user;
	@JsonIgnore
	private Presentation presentation;
}
