package com.lucidiovacas.testing.testingtask;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lucidiovacas.testing.testingtask.StudentMockData.SENIORSTUDENTS;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private StudentService studentService;

    ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    void getAllStudents() throws Exception {
        // given
        Student student1 = new Student(1L, "Jose", "jose@email.com", 22);
        Student student2 = new Student(2L, "Lucas", "lucas@email.com", 35);
        Student student3 = new Student(3L, "Maria", "maria@email.com", 67);
        when(studentService.findAllStudents()).thenReturn(List.of(student1, student2, student3));

        String expectedResult = mapper.writeValueAsString(List.of(student1, student2, student3));

        // when-then
        mvc.perform(get("/api/v1/students/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", equalTo(student1.getName())))
                .andExpect(jsonPath("$[1].email", equalTo(student2.getEmail())))
                .andExpect(content().json(expectedResult));

        verify(studentService, times(1)).findAllStudents();
    }

    @Test
    void getAllSeniorStudents() throws Exception {
        // given
        when(studentService.findAllSeniorStudents()).thenReturn(SENIORSTUDENTS);

        // when - then
        mvc.perform(get("/api/v1/students/seniors"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$",hasSize(SENIORSTUDENTS.size())))
                .andExpect(content().json(mapper.writeValueAsString(SENIORSTUDENTS)));

        verify(studentService, times(1)).findAllSeniorStudents();
    }

    @Test
    void getStudentById() throws Exception {
        // given
        Student student = new Student(1L, "name", "name@email.com", 33);
        when(studentService.findById(student.getId())).thenReturn(student);

        // when - then
        mvc.perform(get("/api/v1/students/{student_id}",1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(student)));

        verify(studentService, times(1)).findById(student.getId());

    }

    @Test
    void getStudentByName_Exists() throws Exception {
        // given
        Student student = new Student(1L, "name", "name@email.com", 33);
        when(studentService.findByName(student.getName())).thenReturn(student);

        // when - then
        mvc.perform(get("/api/v1/students?name={student_name}", student.getName()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(student)));

        verify(studentService, times(1)).findByName(student.getName());
    }

    @Test
    void getStudentByName_DoNotExists() throws Exception {
        // given
        when(studentService.findByName(any(String.class))).thenReturn(null);

        // when - then
        mvc.perform(get("/api/v1/students").param("name", "anyName"))
                .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(""));

        verify(studentService, times(1)).findByName("anyName");
    }

    @Test
    void addStudent() throws Exception {
        // given
        Student studentToAdd = new Student(null, "newStudent", "newStudent@email", 69);
        Student studentExpected = new Student(1L,"newStudent", "newStudent@email", 69);
        when(studentService.addStudent(studentToAdd)).thenReturn(studentExpected);

        // when-then
        mvc.perform(post("/api/v1/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(studentToAdd)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("newStudent"))
                .andExpect(content().json(mapper.writeValueAsString(studentExpected)));

        verify(studentService, times(1)).addStudent(any(Student.class));
    }

    @Test
    void updateStudent_ExistingId() throws Exception {
        // given
        Student studentToUpdate = new Student(null, "newStudent", "newStudent@email", 69);
        when(studentService.updateStudent(studentToUpdate)).thenReturn(studentToUpdate);

        // when - then
        mvc.perform(put("/api/v1/students")
                .content(mapper.writeValueAsString(studentToUpdate))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(studentToUpdate)));
    }

    @Test
    void updateStudent_NotExistingId() throws Exception {
        // given
        Student studentToUpdate = new Student(null, "newStudent", "newStudent@email", 69);
        when(studentService.updateStudent(studentToUpdate)).thenThrow(DoesNotExistException.class);

        // when - then
        mvc.perform(put("/api/v1/students")
                        .content(mapper.writeValueAsString(studentToUpdate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    void partialUpdateStudent_ExistingId() throws Exception {
        // given
        Student student = new Student(1L, "oldName", "email@email.com", 25);
        Map<Object, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "aNewName");
        Student updatedStudent = new Student(1L, "aNewName", "email@email.com", 25);
        when(studentService.partialUpdateStudent(student.getId(), fieldsToUpdate)).thenReturn(updatedStudent);

        // when - then
        mvc.perform(patch("/api/v1/students/{id}", student.getId())
                        .content(mapper.writeValueAsString(fieldsToUpdate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(updatedStudent)));
    }

    @Test
    void partialUpdateStudent_NotExistingId() throws Exception {
        // given
        Student student = new Student(1L, "oldName", "email@email.com", 25);
        Map<Object, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "aNewName");
        Student updatedStudent = new Student(1L, "aNewName", "email@email.com", 25);
        when(studentService.partialUpdateStudent(student.getId(), fieldsToUpdate)).thenThrow(new DoesNotExistException());

        // when - then
        mvc.perform(patch("/api/v1/students/{id}", student.getId())
                        .content(mapper.writeValueAsString(fieldsToUpdate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    void deleteStudent_ExistingId() throws Exception{
        // given
        Student studentToDelete = new Student(1L, "name", "email", 33);
        when(studentService.deleteStudentById(studentToDelete.getId())).thenReturn(studentToDelete);

        // when - then
        mvc.perform(delete("/api/v1/students/{id}", studentToDelete.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(studentToDelete)));

        verify(studentService, times(1)).deleteStudentById(anyLong());
    }

    @Test
    void deleteStudent_NotExistingId() throws Exception{
        // given
        Student studentToDelete = new Student(1L, "name", "email", 33);
        when(studentService.deleteStudentById(studentToDelete.getId())).thenThrow(new DoesNotExistException());

        // when - then
        mvc.perform(delete("/api/v1/students/{id}", studentToDelete.getId()))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));

        verify(studentService, times(1)).deleteStudentById(anyLong());
    }
}