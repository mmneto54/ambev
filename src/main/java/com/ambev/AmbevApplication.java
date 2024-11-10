package com.ambev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AmbevApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmbevApplication.class, args);
	}

}
