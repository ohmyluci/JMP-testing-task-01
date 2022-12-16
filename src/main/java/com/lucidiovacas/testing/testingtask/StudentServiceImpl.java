package com.lucidiovacas.testing.testingtask;

import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service
public class StudentServiceImpl implements StudentService{
    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student findByName(String name) {
        return studentRepository.findByName(name);
    }

    @Override
    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Student deleteStudentById(Long id) throws DoesNotExistException {
        Student student = checkStudentExistsById(id);
        studentRepository.deleteById(id);
        return student;
    }

    @Override
    public Student updateStudent(Student student) throws DoesNotExistException {
        checkStudentExistsById(student.getId());
        return studentRepository.save(student);
    }

    @Override
    public Student partialUpdateStudent(Long id, Map<Object, Object> fieldsToUpdate) throws DoesNotExistException {
        Student student = checkStudentExistsById(id);
        fieldsToUpdate.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Student.class, (String) key);
            field.setAccessible(true);
            ReflectionUtils.setField(field, student, value);
        });
        return studentRepository.save(student);
    }

    private Student checkStudentExistsById(Long id) throws DoesNotExistException {
        return studentRepository.findById(id).orElseThrow(() ->
                new DoesNotExistException(String.format("Student with id %d does not exist", id)));
    }

    @Override
    public Student findById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    @Override
    public List<Student> findAllSeniorStudents() {
        return studentRepository.findSeniorStudents();
    }


}
