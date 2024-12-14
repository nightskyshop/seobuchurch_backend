package com.example.seobuchurch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SeobuchurchApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeobuchurchApplication.class, args);
	}

}
