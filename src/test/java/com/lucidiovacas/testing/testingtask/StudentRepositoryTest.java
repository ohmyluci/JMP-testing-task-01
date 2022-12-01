package com.lucidiovacas.testing.testingtask;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepo;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
        studentRepo.deleteAll();
        System.out.println("After");
        System.out.println(studentRepo.findAll());
    }

    @Test
    void checkFindSeniorStudents() {
        // given
        Student student1 = new Student(null, "Jose", "jose@email.com", 22);
        Student student2 = new Student(null, "Lucas", "lucas@email.com", 35);
        Student student3 = new Student(null, "Marta", "marta@email.com", 71);
        studentRepo.saveAll(List.of(student1,student2,student3));

        // when
        List<Student> seniorStudents = studentRepo.findSeniorStudents();

        // then
        assertEquals(1, seniorStudents.size());
        assertEquals(seniorStudents.get(0).getName(), student3.getName());
        System.out.println(student3.getId());
    }
}