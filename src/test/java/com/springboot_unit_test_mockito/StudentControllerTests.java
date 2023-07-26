package com.springboot_unit_test_mockito;

import com.springboot_unit_test_mockito.controller.StudentController;
import com.springboot_unit_test_mockito.entity.Student;
import com.springboot_unit_test_mockito.server.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class StudentControllerTests {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetStudentById_Success() {
        UUID studentId = UUID.randomUUID();
        Student student = new Student(studentId, "John Doe", 25);

        when(studentService.getStudentById(studentId)).thenReturn(Optional.of(student));

        ResponseEntity<?> response = studentController.getStudentById(studentId.toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(student, response.getBody());
        verify(studentService, times(1)).getStudentById(studentId);
    }

    @Test
    public void testGetStudentById_InvalidIdFormat() {
        ResponseEntity<?> response = studentController.getStudentById("invalid-id-format");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verifyNoInteractions(studentService);
    }

    @Test
    public void testGetStudentById_StudentNotFound() {
        UUID studentId = UUID.randomUUID();

        when(studentService.getStudentById(studentId)).thenReturn(Optional.empty());

        ResponseEntity<?> response = studentController.getStudentById(studentId.toString());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(studentService, times(1)).getStudentById(studentId);
    }

    @Test
    public void testCreateBulkStudents_Success() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(UUID.randomUUID(), "Alice", 22));
        students.add(new Student(UUID.randomUUID(), "Bob", 23));

        when(studentService.saveAllStudents(any())).thenReturn(students);

        ResponseEntity<?> response = studentController.createBulkStudents(students);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(students, response.getBody());
        verify(studentService, times(1)).saveAllStudents(students);
    }

    @Test
    public void testCreateBulkStudents_UnexpectedError() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(UUID.randomUUID(), "Alice", 22));
        students.add(new Student(UUID.randomUUID(), "Bob", 23));

        when(studentService.saveAllStudents(any())).thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<?> response = studentController.createBulkStudents(students);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(studentService, times(1)).saveAllStudents(students);
    }
}
