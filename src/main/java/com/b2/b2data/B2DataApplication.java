package com.b2.b2data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.b2.b2data.repository")
@EntityScan("com.b2.b2data.domain")
//@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
public class B2DataApplication {

	public static void main(String[] args) {
		SpringApplication.run(B2DataApplication.class, args);
	}

}
