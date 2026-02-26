package com.sistemavocacional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class SistemaVocacionalApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaVocacionalApplication.class, args);
	}

}
