package com.lucidiovacas.testing.testingtask;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class DtoMapper {

    public Student toStudent(StudentDto studentDto) {
        return new Student(studentDto.getId(), studentDto.getName(), studentDto.getEmail(), studentDto.getAge());
    }
}
