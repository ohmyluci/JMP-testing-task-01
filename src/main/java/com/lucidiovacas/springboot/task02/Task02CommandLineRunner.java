package com.lucidiovacas.springboot.task02;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Task02CommandLineRunner implements CommandLineRunner {

    @Autowired
    private StudentRepository studentRepo;

    @Override
    public void run(String... args) throws Exception {
        Student newStudent = new Student(2L, "Lucidio", "lucidiovacas@gmail.com");
        studentRepo.save(newStudent);
        System.out.println(studentRepo.findById(2L));

        Student updatedStudent = new Student(2L, "Lucidio", "anewemail@gmail.com");
        studentRepo.save(updatedStudent);
        System.out.println(studentRepo.findById(2L));

        studentRepo.deleteById(2L);
        System.out.println(studentRepo.findById(2L));
    }
}
