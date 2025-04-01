package com.tom.example.batch.student;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "student")
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "first_name", nullable = true, updatable = true)
	private String firstName;
	
	@Column(name = "last_name", nullable = true, updatable = true)
	private String lastName;
	
	@Column(name = "age", nullable = true, updatable = true)
	private int age;
	
}
