package com.springboot_unit_test_mockito;

import com.springboot_unit_test_mockito.dao.StudentDao;
import com.springboot_unit_test_mockito.entity.Student;
import com.springboot_unit_test_mockito.server.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class StudentServiceComponentTest {

    @Mock
    private StudentDao studentDao;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetStudentById() {
        UUID studentId = UUID.randomUUID();
        String studentName = "John Doe";
        int studentAge = 25;

        // Create a mock Student object
        Student mockStudent = new Student();
        mockStudent.setId(studentId);
        mockStudent.setName(studentName);
        mockStudent.setAge(studentAge);

        // Configure the studentDao mock to return the mockStudent when findById is called
        when(studentDao.findById(studentId)).thenReturn(Optional.of(mockStudent));

        // Call the getStudentById method of the studentService
        Optional<Student> result = studentService.getStudentById(studentId);

        // Assert that the result matches the mockStudent
        assertEquals(mockStudent, result.get());
    }
}
