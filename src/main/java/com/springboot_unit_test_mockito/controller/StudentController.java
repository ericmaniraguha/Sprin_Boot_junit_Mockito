package com.springboot_unit_test_mockito.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.springboot_unit_test_mockito.entity.Student;
import com.springboot_unit_test_mockito.exception.ErrorResponse;
import com.springboot_unit_test_mockito.exception.ResourceNotFoundException;
import com.springboot_unit_test_mockito.server.StudentService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("student/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable String id) {
        try {
            UUID uuid = UUID.fromString(id);
            Optional<Student> student = studentService.getStudentById(uuid);

            if (student.isPresent()) {
                return ResponseEntity.ok(student.get());
            } else {
                throw new ResourceNotFoundException("Student not found with ID: " + id);
            }
        } catch (IllegalArgumentException ex) {
            // If the provided ID is not a valid UUID
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Invalid student ID format"));
        } catch (ResourceNotFoundException ex) {
            // If the student is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
        } catch (Exception ex) {
            // For other unexpected exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new ErrorResponse("An unexpected error occurred"));
        }
    }

    @PostMapping("students/bulk")
    public ResponseEntity<?> createBulkStudents(@RequestBody List<Student> students) {
        try {
            List<Student> savedStudents = studentService.saveAllStudents(students);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedStudents);
        } catch (Exception ex) {
            // For unexpected exceptions during bulk student creation
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new ErrorResponse("An unexpected error occurred"));
        }
    }

    @PutMapping("student/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable String id, @RequestBody Student updatedStudent) {
        try {
            UUID uuid = UUID.fromString(id);
            Student existingStudent = studentService.getStudentById(uuid)
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));

            existingStudent.setName(updatedStudent.getName());
            existingStudent.setAge(updatedStudent.getAge());

            Student updated = studentService.saveStudent(existingStudent);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException ex) {
            // If the provided ID is not a valid UUID
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Invalid student ID format"));
        } catch (ResourceNotFoundException ex) {
            // If the student is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
        } catch (Exception ex) {
            // For other unexpected exceptions during student update
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new ErrorResponse("An unexpected error occurred"));
        }
    }

    @DeleteMapping("deleteStudent/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable String id) {
        try {
            UUID uuid = UUID.fromString(id);
            studentService.deleteStudentById(uuid);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Student with ID " + id + " has been deleted successfully.");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            // If the provided ID is not a valid UUID
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Invalid student ID format"));
        } catch (ResourceNotFoundException ex) {
            // If the student is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
        } catch (Exception ex) {
            // For other unexpected exceptions during student deletion
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("An unexpected error occurred"));
        }
    }


}
