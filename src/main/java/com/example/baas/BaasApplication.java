package com.example.baas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class BaasApplication {

	public static void main(String[] args) {
		SpringApplication.run(BaasApplication.class, args);
	}
}