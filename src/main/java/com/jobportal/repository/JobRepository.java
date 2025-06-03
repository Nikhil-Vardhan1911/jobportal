package com.jobportal.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jobportal.entity.Job;

public interface JobRepository extends JpaRepository<Job, Long> {
	
	// Search + Pagination support in a single method
	Page<Job> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);

}
