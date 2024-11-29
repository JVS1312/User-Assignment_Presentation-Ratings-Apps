package com.jspider.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jspider.Entity.Presentation;

public interface PresentationRepository extends JpaRepository<Presentation, Integer>{

	List<Presentation> findByUserId(Integer studentId);

}
