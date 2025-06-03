package com.jobportal.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jobportal.dto.JobRequest;
import com.jobportal.entity.Job;

public interface JobService {

	Job createJob(JobRequest  jobRequest);
	Job getJobById(Long id);
	Page<Job> getAllJobs(String keyword, Pageable pageable);
	Job updateJob(Long id, JobRequest jobRequest);
	void deleteJob(Long id);
}
