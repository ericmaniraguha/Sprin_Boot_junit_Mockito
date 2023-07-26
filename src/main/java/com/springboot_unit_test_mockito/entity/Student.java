package com.springboot_unit_test_mockito.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "students")
public class Student {
    @Id
    private UUID id;
    private String name;
    private int age;

    // Constructors

    public Student() {
        this.id = UUID.randomUUID();
    }
    
    

    public Student(UUID id, String name, int age) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
	}



	// Getters and setters for the UUID field should handle String representations

    public String getId() {
        return id.toString();
    }

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public void setId(String id) {
        this.id = UUID.fromString(id);
    }
}
