package com.fnkcode.postis;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAware")
@OpenAPIDefinition(
		info = @Info(
				title = "POSTIS API - DOCUMENTATION",
				description = "Asignment for job application . Vacation requests API",
				version = "v1",
				contact = @Contact(
						name = "Visan Florin-Daniel",
						email = "florinvisan022@gmail.com"
				)
		)
)
public class PostisApplication {

	public static void main(String[] args) {
		SpringApplication.run(PostisApplication.class, args);
	}

}
