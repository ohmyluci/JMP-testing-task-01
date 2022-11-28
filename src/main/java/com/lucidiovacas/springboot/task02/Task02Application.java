package com.lucidiovacas.springboot.task02;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Task02Application {

//	@Autowired
//	private StudentRepository studentRepo;
//
//	@Bean
//	public CommandLineRunner run() {
//		return args -> {
//			System.out.println(studentRepo.findAll());
//		};
//	}

	public static void main(String[] args) {
		SpringApplication.run(Task02Application.class, args);
	}

}
