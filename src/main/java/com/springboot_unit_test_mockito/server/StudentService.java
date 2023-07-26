package com.springboot_unit_test_mockito.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot_unit_test_mockito.dao.StudentDao;
import com.springboot_unit_test_mockito.entity.Student;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StudentService {

    @Autowired
    private StudentDao studentDao;

    public List<Student> getAllStudents() {
        return studentDao.findAll();
    }

    public Optional<Student> getStudentById(UUID id) {
        return studentDao.findById(id);
    }

    public Student saveStudent(Student student) {
        return studentDao.save(student);
    }

    public void deleteStudentById(UUID id) {
        studentDao.deleteById(id);
    }
}
