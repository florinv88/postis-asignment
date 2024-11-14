package com.fnkcode.postis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAware")
public class PostisApplication {

	public static void main(String[] args) {
		SpringApplication.run(PostisApplication.class, args);
	}

}
