package com.springboot_unit_test_mockito.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.springboot_unit_test_mockito.entity.Student;

import java.util.UUID;

public interface StudentDao extends MongoRepository<Student, UUID> {
}
