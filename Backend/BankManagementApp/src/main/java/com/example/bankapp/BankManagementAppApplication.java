package com.example.bankapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BankManagementAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankManagementAppApplication.class, args);
	}

}
