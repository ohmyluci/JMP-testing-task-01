package com.lucidiovacas.testing.testingtask;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByName(String name);

    @Query("SELECT s FROM Student s WHERE s.age > 55")
    List<Student> findSeniorStudents();
}
