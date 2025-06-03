package com.jobportal.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.jobportal.dto.JobRequest;
import com.jobportal.entity.Job;
import com.jobportal.entity.JobType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JobControllerIntegration {
	
	@LocalServerPort
	private int port;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Test
	public void testCreateJobIntegration() {
		String url = "http://localhost:"+port + "/api/jobs";
		
		JobRequest jobRequest = new JobRequest();
		jobRequest.setTitle("Java Developer");
		jobRequest.setDescription("Experienced Java Backend dev");
		jobRequest.setCompanyName("TCS");
		jobRequest.setJobType(JobType.FULL_TIME);
		jobRequest.setLocation("Hyderabad");
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<JobRequest> request = new HttpEntity<>(jobRequest, headers);
		ResponseEntity<Job>response = restTemplate.postForEntity(url,request, Job.class);
		
		assertEquals(HttpStatus.OK,response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("TCS", response.getBody().getCompanyName());
	}
	
	
	@Test
	public void testGetJobByIdIntegration() {
		
		String url = "http://localhost:"+port + "/api/jobs";
		
		JobRequest jobrequest = new JobRequest();
		jobrequest.setTitle("test get");
		jobrequest.setDescription("Testing get by ID");
		jobrequest.setCompanyName("Infosys");
		jobrequest.setJobType(JobType.FULL_TIME);
		jobrequest.setLocation("Bangalore");
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<JobRequest> request = new HttpEntity<>(jobrequest, headers);
		
		ResponseEntity<Job>postResponse = restTemplate.postForEntity(url, request, Job.class);
		Long createJobId = postResponse.getBody().getId();
		
		String getUrl = url + "/" + createJobId;
		ResponseEntity<Job>getResponse = restTemplate.getForEntity(getUrl, Job.class);
		
		assertEquals(HttpStatus.OK,getResponse.getStatusCode());
		assertEquals("Infosys",getResponse.getBody().getCompanyName());
	}
	
	@Test
	public void testUpdateJobIntegration() {
		
		String url = "http://localhost:"+port+"/api/jobs";
		
		JobRequest jobrequest = new JobRequest();
		jobrequest.setTitle("OldTitle");
		jobrequest.setDescription("Old description");
		jobrequest.setCompanyName("Old Company");
		jobrequest.setJobType(JobType.FULL_TIME);
		jobrequest.setLocation("Delhi");
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<JobRequest>request = new HttpEntity<>(jobrequest,headers);
		ResponseEntity<Job>postResponse = restTemplate.postForEntity(url, request, Job.class);
		
		Long jobId = postResponse.getBody().getId();
		
		
		// Step 2: prepare update playLoad
		Job updateJob = Job.builder()
				.id(jobId)
				.title("New Title")
				.description("New Description")
				.companyName("New Company")
				.jobType(JobType.PART_TIME)
				.location("Mumbai")
				.build();
		
		HttpEntity<Job> updateRequest = new HttpEntity<>(updateJob, headers);
		ResponseEntity<Job> putResponse = restTemplate.exchange(url+ "/"+ jobId,HttpMethod.PUT,updateRequest,Job.class);
		
		assertEquals(HttpStatus.OK,putResponse.getStatusCode());
		assertEquals("New Title", putResponse.getBody().getTitle());
		assertEquals(JobType.PART_TIME, putResponse.getBody().getJobType());
	}
	
	@Test
	public void testDeleteJobIntegration() {
		
		String url = "http://localhost:"+ port + "/api/jobs";
		
		JobRequest jobrequest = new JobRequest();
		jobrequest.setTitle("Delete me");
		jobrequest.setDescription("To be deleted");
		jobrequest.setCompanyName("DeleteCo");
		jobrequest.setJobType(JobType.CONTRACT);
		jobrequest.setLocation("Remote");
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<JobRequest> request = new HttpEntity<>(jobrequest,headers);
		ResponseEntity<Job> postResponse = restTemplate.postForEntity(url,request,Job.class);
		Long createdId = postResponse.getBody().getId();
		
		//Step 2 Delete the job
		restTemplate.delete(url + "/"+ createdId);
		
		
		// Try get again it should fail
		ResponseEntity<String> getResponse = restTemplate.getForEntity(url+"/"+ createdId, String.class);
		assertEquals(HttpStatus.NOT_FOUND,getResponse.getStatusCode());
	}
	
	@Test
	public void testValidationFailure() {
		
		String url = "http://localhost:"+ port + "/api/jobs";
		
		JobRequest jobrequest = new JobRequest();
		jobrequest.setDescription("Missing title field");
		jobrequest.setCompanyName("Oops");
		jobrequest.setJobType(JobType.FULL_TIME);
		jobrequest.setLocation("Now where");
		
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<JobRequest> request = new HttpEntity<>(jobrequest,headers);
		ResponseEntity<String>response = restTemplate.postForEntity(url,request,String.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().contains("title")); // check if error method contains field name
	}
}