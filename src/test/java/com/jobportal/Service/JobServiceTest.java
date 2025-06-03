package com.jobportal.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.jobportal.dto.JobRequest;
import com.jobportal.entity.Job;
import com.jobportal.entity.JobType;
import com.jobportal.repository.JobRepository;
import com.jobportal.service.JobService;
import com.jobportal.service.JobServiceImpl;

public class JobServiceTest {

	@Mock
	private JobRepository jobRepository;
	
	@InjectMocks
	private JobServiceImpl jobService;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	void testCreateJob() {
		JobRequest request = new JobRequest("Java Dev", "2+ years experience", "IBM", JobType.FULL_TIME,"chicago");
		
		Job job = Job.builder().
				  id(1L)
				  .title(request.getTitle())
				  .description(request.getDescription())
				  .companyName(request.getCompanyName())
				  .jobType(request.getJobType())
				  .location(request.getLocation())
				  .createdAt(LocalDateTime.now())
				  .updatedAt(LocalDateTime.now())
				  .build();
		
		when(jobRepository.save(any(Job.class))).thenReturn(job);
		
		Job savedJob = jobService.createJob(request);
		
		//Assert
		assertEquals(request.getCompanyName(),savedJob.getCompanyName());
		assertEquals(JobType.FULL_TIME, savedJob.getJobType());
		verify(jobRepository,times(1)).save(any(Job.class));
	}
	@Test
	public void testGetJobById() {
		
		Long id = 1L;
		
		Job job = Job.builder()
				.id(id)
				.title("Java Developer")
				.description("2+ years Experience")
				.companyName("IBM")
				.jobType(JobType.FULL_TIME)
				.location("chicago")
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		
		when(jobRepository.findById(id)).thenReturn(Optional.of(job));
		
		Job foundJob = jobService.getJobById(id);
		
		//Assert
		assertEquals(id,foundJob.getId());
		assertEquals("IBM", foundJob.getCompanyName());
		verify(jobRepository,times(1)).findById(id);
	}
}
