package com.example.prs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.prs.model.User;
import com.example.prs.service.OrderService;
import com.example.prs.service.UserService;

@SpringBootApplication
public class PhoneRepairServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhoneRepairServiceApplication.class, args);
	}
}
