package com.jspider.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jspider.Entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PresentationDTO {
	private Integer pid;
	private String topic;
	private Double userTotalScore;
	@JsonIgnore
	private User user;
	
}
