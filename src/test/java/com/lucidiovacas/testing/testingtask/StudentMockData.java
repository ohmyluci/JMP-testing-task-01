package com.lucidiovacas.testing.testingtask;

import java.util.ArrayList;
import java.util.List;

public class StudentMockData {

    public static final List<Student> SENIORSTUDENTS = new ArrayList<>(
            List.of(new Student(1L, "lucidio", "lucidio@email.com", 68),
                    new Student(2L, "pedro", "pedro@email.com", 56))
    );
}
