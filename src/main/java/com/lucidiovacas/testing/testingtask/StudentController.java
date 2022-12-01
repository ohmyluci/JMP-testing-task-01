package com.lucidiovacas.testing.testingtask;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/all")
    public ResponseEntity<List<Student>> getAllStudents() {
        log.info("Get all students");
        return new ResponseEntity<>(studentService.findAllStudents(), HttpStatus.OK);
    }

    @GetMapping("/seniors")
    public ResponseEntity<List<Student>> getAllSeniorStudents() {
        log.info("Get all senior students");
        return new ResponseEntity<>(studentService.findAllSeniorStudents(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        log.info("Get user with id {}", id);
        return new ResponseEntity<>(studentService.findById(id), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<Student> getStudentByName(@RequestParam String name) {
        log.info("Get student by name {}", name);
        return new ResponseEntity<>(studentService.findByName(name), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        log.info("Add new student with name {}", student.getName());
        return new ResponseEntity<>(studentService.addStudent(student), HttpStatus.CREATED);
    }

    @PutMapping("")
    public ResponseEntity<Student> updateStudent(@RequestBody Student student) {
        log.info("Update student with id {}", student.getId());
        try {
            return new ResponseEntity<>(studentService.updateStudent(student), HttpStatus.OK);
        } catch (DoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Student> partialUpdateStudent(@PathVariable Long id, @RequestBody Map<Object, Object> fields) {
        log.info("Partial update student with id {}", id);
        try {
            return new ResponseEntity<>(studentService.partialUpdateStudent(id, fields), HttpStatus.OK);
        } catch (DoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Student with id {} does not exist", id));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable Long id) {
        log.info("Delete student with id {}", id);
        try {
            return new ResponseEntity<>(studentService.deleteStudentById(id), HttpStatus.OK);
        } catch (DoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

    }

}
