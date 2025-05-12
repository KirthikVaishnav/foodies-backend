package com.kirthikv.backend_foodies_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.kirthikv.backend_foodies_app"})
public class BackendFoodiesAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendFoodiesAppApplication.class, args);
	}

}
