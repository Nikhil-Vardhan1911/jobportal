package com.jobportal.dto;

import com.jobportal.entity.JobType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobRequest {
	
	@NotBlank(message = "Title is required")
	private String title;
	
	@NotBlank(message = "Description is required")
	@Size(min = 10, message = "Description must be atleast 10 characters")
	private String description;
	
	@NotBlank(message = "Company name is required")
	private String companyName;
	
	@Enumerated(EnumType.STRING)
	@NotNull(message = "Job type is required")
	private JobType jobType;
	
	@NotBlank(message = "Location is required")
	private String location;
}
