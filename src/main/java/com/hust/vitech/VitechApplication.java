package com.hust.vitech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication()
@EnableJpaAuditing
@EntityScan(basePackages = {"com.hust.vitech.Model"})
public class VitechApplication {

	public static void main(String[] args) {
		SpringApplication.run(VitechApplication.class, args);
	}

}
