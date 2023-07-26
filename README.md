# Sprin_Boot_junit_Mockito

# Spring Boot CRUD App with MongoDB and UUID

This project is a Spring Boot CRUD application that uses MongoDB as the database and UUID as the ID format for the Student entity.

## Tasks Performed

- [x] Set up the Spring Boot project.
- [x] Define the Student entity class with UUID as the ID format.
- [x] Create the StudentDao interface for data access.
- [x] Implement the StudentService class for business logic.
- [x] Create the StudentController for handling HTTP requests.
- [x] Handle exceptions and create a custom ResourceNotFoundException class.
- [x] Configure MongoDB and the application properties.
- [x] Test the CRUD operations and validate the application.

## Code Snippets

```java
// Student Entity
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "students")
public class Student {
    @Id
    private UUID id;
    private String name;
    private int age;

    // Constructors, getters, setters, etc.
}

// Student Service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StudentService {
    // ...

    public Optional<Student> getStudentById(UUID id) {
        return studentDao.findById(id);
    }

    // ...
}

// Student Controller
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/students")
public class StudentController {
    // ...

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

    // ...
}
