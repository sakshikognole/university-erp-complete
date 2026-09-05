package com.example.demo.student.exception;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(String studentId) {
        super("Student not found with ID: " + studentId);
    }
}
