package com.lucidiovacas.testing.testingtask;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class StudentControllerIntegrationTest {

    @Autowired
    private TestRestTemplate client;

    @LocalServerPort
    private int port;

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    void addStudent() throws JsonProcessingException {
        Student student1 = new Student(1L, "name1", "email1", 21);
        Student student2 = new Student(2L, "name2", "email2", 23);

//        // I think this one is a better way
//        ResponseEntity<Student> response = client
//                .postForEntity("/api/v1/students", student1, Student.class);

        ResponseEntity<String> response = client
                .postForEntity("/api/v1/students", student1, String.class);

        String json = response.getBody();

        JsonNode jsonNode = mapper.readTree(json);
        assertEquals("email1", jsonNode.path("email").asText());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("{\"id\":1,\"name\":\"name1\",\"email\":\"email1\",\"age\":21}", json);

        response = client
                .postForEntity(createUri("/api/v1/students"), student2, String.class);

        json = response.getBody();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals("{\"id\":2,\"name\":\"name2\",\"email\":\"email2\",\"age\":23}", json);

    }

    @Test
    @Order(2)
    void getAllStudents() {

        ResponseEntity<Student[]> response = client
                .getForEntity("/api/v1/students/all", Student[].class);

        List<Student> students = Arrays.asList(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, students.size());
        assertEquals("name1", students.get(0).getName());
    }

    private String createUri(String uri) {
        return "http://localhost:" + port + uri;
    }
}