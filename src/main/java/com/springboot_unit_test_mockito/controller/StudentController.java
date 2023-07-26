package com.springboot_unit_test_mockito.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.springboot_unit_test_mockito.entity.Student;
import com.springboot_unit_test_mockito.exception.ResourceNotFoundException;
import com.springboot_unit_test_mockito.server.StudentService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable String id) {
        UUID uuid = UUID.fromString(id);
        Optional<Student> student = studentService.getStudentById(uuid);

        if (student.isPresent()) {
            return ResponseEntity.ok(student.get());
        } else {
            throw new ResourceNotFoundException("Student not found with ID: " + id);
        }
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student savedStudent = studentService.saveStudent(student);
        return new ResponseEntity<>(savedStudent, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable String id, @RequestBody Student updatedStudent) {
        UUID uuid = UUID.fromString(id);
        Student existingStudent = studentService.getStudentById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));

        existingStudent.setName(updatedStudent.getName());
        existingStudent.setAge(updatedStudent.getAge());

        Student updated = studentService.saveStudent(existingStudent);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String id) {
        UUID uuid = UUID.fromString(id);
        studentService.deleteStudentById(uuid);
        return ResponseEntity.noContent().build();
    }
}
