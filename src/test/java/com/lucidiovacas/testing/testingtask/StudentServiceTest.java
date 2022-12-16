package com.lucidiovacas.testing.testingtask;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.BDDMockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

//@ExtendWith(MockitoExtension.class)
@SpringBootTest
class StudentServiceTest {

    @MockBean
    private StudentRepository studentRepository;

    @Autowired
    private StudentService studentService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void findAllStudents() {
        // when
        studentService.findAllStudents();
        // then
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void canAddStudent() {
        // given
        Student student = new Student(null, "Marcos", "marcos@email.com", 33);
        when(studentRepository.save(student)).then(invocation -> {
            Student studentSaved = invocation.getArgument(0);
            studentSaved.setId(1L);
            return studentSaved;
        });

        // when
        Student resultStudent = studentService.addStudent(student);

        // then
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);

        verify(studentRepository, times(1)).save(studentArgumentCaptor.capture());
        assertEquals(1L, resultStudent.getId());
        assertEquals(student.getName(), studentArgumentCaptor.getValue().getName());
    }

    @Test
    void findByName() {
        //given
        Student student = new Student(null, "Marcos", "marcos@email.com", 33);
        when(studentRepository.findByName(student.getName())).thenReturn(student);

        // when
        Student resultStudent = studentService.findByName(student.getName());

        // then
        verify(studentRepository, times(1)).findByName(student.getName());
        assertEquals(student, resultStudent);
    }


    @Test
    void deleteStudentById_whenExists() throws DoesNotExistException {
        // given
        Student student = new Student(1L, "Lucidio", "lucidio@email.com", 37);
        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));
        doNothing().when(studentRepository).deleteById(student.getId());

        // when
        studentService.deleteStudentById(student.getId());

        // then
        verify(studentRepository, times(1)).findById(student.getId());
        verify(studentRepository, times(1)).deleteById(student.getId());
    }

    @Test
    void deleteStudentById_whenNotExists() throws DoesNotExistException {
        // given
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        // when then
        Assertions.assertThrows(DoesNotExistException.class, ()-> studentService.deleteStudentById(1L));

        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, never()).deleteById(1L);
    }

    @Test
    void updateStudent_whenExists() throws DoesNotExistException {
        // given
        Student student = new Student(1L, "Lucidio", "lucidio@email.com", 37);
        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));
        when(studentRepository.save(student)).thenReturn(student);

        // when
        studentService.updateStudent(student);

        // then
        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void partialUpdateStudent_whenExists() throws DoesNotExistException {
        // given
        Student student = new Student(1L, "Lucidio", "lucidio@email.com", 37);
        Student expectedUpdatedStudent = new Student(1L, "Lucidio", "anewemail@email.com", 37);
        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));

        when(studentRepository.save(expectedUpdatedStudent)).thenReturn(expectedUpdatedStudent);
        Map<Object, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("email", "anewemail@email.com");

        // when
        Student updatedStudent = studentService.partialUpdateStudent(student.getId(), fieldsToUpdate);

        // then
        verify(studentRepository, times(1)).findById(student.getId());
        verify(studentRepository, times(1)).save(expectedUpdatedStudent);
        assertEquals(expectedUpdatedStudent, student);
    }

    @Test
    void partialUpdateStudent_whenNotExists() throws DoesNotExistException {
        // given
        Student student = new Student(1L, "Lucidio", "lucidio@email.com", 37);
        Student expectedUpdatedStudent = new Student(1L, "Lucidio", "anewemail@email.com", 37);
        when(studentRepository.findById(student.getId())).thenReturn(Optional.empty());

        Map<Object, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("email", "anewemail@email.com");

        // when
        Assertions.assertThrows(DoesNotExistException.class, () ->
                studentService.partialUpdateStudent(student.getId(), fieldsToUpdate));

        // then
        verify(studentRepository, times(1)).findById(student.getId());
        verify(studentRepository, never()).save(expectedUpdatedStudent);
    }

    @Test
    void findById_WhenExists() {
        // given
        Student student = new Student(1L, "Lucidio", "lucidio@email.com", 37);
        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));

        // when
        Student studentResult = studentService.findById(1L);

        // then
        verify(studentRepository, times(1)).findById(student.getId());
        assertEquals(student, studentResult);
    }

    @Test
    void findById_WhenNotExists() {
        // given
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        // when
        Student studentResult = studentService.findById(1L);

        // then
        verify(studentRepository, times(1)).findById(1L);
        assertEquals(null, studentResult);
    }

    @Test
    void findAllSeniorStudents() {
        // given
        Student student1 = new Student(1L, "Juana", "juana@email.com", 56);
        Student student2 = new Student(2L, "Pedro", "pedro@email.com", 58);
        when(studentRepository.findSeniorStudents()).thenReturn(List.of(student1, student2));

        // when
        List<Student> seniorStudentsResult = studentService.findAllSeniorStudents();

        // then
        verify(studentRepository, times(1)).findSeniorStudents();
        assertEquals(2, seniorStudentsResult.size());
        assertEquals(student1, seniorStudentsResult.stream().filter(s -> s.getName().equals("Juana")).findFirst().get());

    }
}