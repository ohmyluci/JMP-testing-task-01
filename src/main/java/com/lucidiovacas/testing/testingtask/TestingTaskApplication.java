package com.lucidiovacas.testing.testingtask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestingTaskApplication {

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
		SpringApplication.run(TestingTaskApplication.class, args);
	}

}
