package com.jobportal.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jobportal.Exception.ResourceNotFoundException;
import com.jobportal.dto.JobRequest;
import com.jobportal.entity.Job;
import com.jobportal.repository.JobRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {
	
	
	private final JobRepository jobRepository;

	@Override
	public Job createJob(JobRequest jobRequest){
		Job job = Job.builder()
				 .title(jobRequest.getTitle())
				 .description(jobRequest.getDescription())
				 .companyName(jobRequest.getCompanyName())
				 .location(jobRequest.getLocation())
				 .jobType(jobRequest.getJobType())
				 
				 .build();
		return jobRepository.save(job);
	}

	@Override
	public Job getJobById(Long id) {
		return jobRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Job not found with id:" + id));
	}

	@Override
	public Job updateJob(Long id, JobRequest request) {
		Job job = jobRepository.findById(id).
				orElseThrow(() -> new ResourceNotFoundException("Job not found with id:" + id));
		job.setTitle(request.getTitle());
		job.setDescription(request.getDescription());
		job.setCompanyName(request.getCompanyName());
		job.setLocation(request.getLocation());
		job.setJobType(request.getJobType());
		
		return jobRepository.save(job);
	}
	@Override
	public void deleteJob(Long id) {
		jobRepository.delete(getJobById(id));	
	}
	
	@Override
	public Page<Job> getAllJobs(String keyword, Pageable pageable) {
		if(keyword == null || keyword.isEmpty()) {
			return jobRepository.findAll(pageable); // No filtering, just pagination
		}
		else {
		return jobRepository.findByTitleContainingIgnoreCase(keyword, pageable);
		}
	}
	
}
