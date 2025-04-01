package com.tom.example.batch.config;

import org.springframework.batch.item.ItemProcessor;

import com.tom.example.batch.student.Student;

public class StudentProcessor implements ItemProcessor<Student, Student> {

	@Override
	public Student process(Student student) {
		return student;
	}
	
}
