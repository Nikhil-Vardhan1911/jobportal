package com.jobportal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

	
	@Bean
	public OpenAPI baseOpenAPI() {
		return new OpenAPI().info(new Info()
				.title("Job Portal API")
				.version("1.0.0")
				.description("API docs for your Job Portal application"));
	}
}
