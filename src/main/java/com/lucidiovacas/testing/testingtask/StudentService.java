package com.lucidiovacas.testing.testingtask;

import java.util.List;
import java.util.Map;

public interface StudentService {

    public List<Student> findAllStudents();
    public Student findByName(String name);
    public Student addStudent(Student student);
    public Student deleteStudentById(Long id) throws DoesNotExistException;
    Student updateStudent(Student student) throws DoesNotExistException;
    Student partialUpdateStudent(Long id, Map<Object, Object> fieldsToUpdate) throws DoesNotExistException;
    Student findById(Long id);

    List<Student> findAllSeniorStudents();
}
