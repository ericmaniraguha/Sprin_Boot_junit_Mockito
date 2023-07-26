package com.springboot_unit_test_mockito;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot_unit_test_mockito.controller.StudentController;
import com.springboot_unit_test_mockito.entity.Student;
import com.springboot_unit_test_mockito.server.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
public class StudentControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllStudents() throws Exception {
        List<Student> students = new ArrayList<>();
        students.add(new Student(UUID.randomUUID(), "John Doe", 25));

        when(studentService.getAllStudents()).thenReturn(students);

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(students)));

        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    public void testGetStudentById_Success() throws Exception {
        UUID studentId = UUID.randomUUID();
        Student student = new Student(studentId, "John Doe", 25);

        when(studentService.getStudentById(studentId)).thenReturn(Optional.of(student));

        mockMvc.perform(get("/student/" + studentId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(student)));

        verify(studentService, times(1)).getStudentById(studentId);
    }

    @Test
    public void testGetStudentById_StudentNotFound() throws Exception {
        UUID studentId = UUID.randomUUID();

        when(studentService.getStudentById(studentId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/student/" + studentId))
                .andExpect(status().isNotFound());

        verify(studentService, times(1)).getStudentById(studentId);
    }

    @Test
    public void testCreateBulkStudents_Success() throws Exception {
        List<Student> students = new ArrayList<>();
        students.add(new Student(UUID.randomUUID(), "Alice", 22));
        students.add(new Student(UUID.randomUUID(), "Bob", 23));

        when(studentService.saveAllStudents(any())).thenReturn(students);

        mockMvc.perform(post("/students/bulk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(students)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(students)));

        verify(studentService, times(1)).saveAllStudents(any());
    }
}
