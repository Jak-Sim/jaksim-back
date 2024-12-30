package com.example.jaksim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class JakSimApplication {

	public static void main(String[] args) {
		SpringApplication.run(JakSimApplication.class, args);
	}

}
