package com.jspider.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jspider.Entity.Ratings;
import com.jspider.Entity.User;

public interface RatingRepository extends JpaRepository<Ratings	, Integer>{

	List<Ratings> findByUserId(Integer studentId);

	 Optional<Ratings> findByPresentationPid(Integer pid);

}
