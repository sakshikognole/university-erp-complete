package com.example.demo.student.service;

import com.example.demo.student.dto.StudentResponse;
import com.example.demo.student.exception.StudentNotFoundException;
import com.example.demo.student.model.Student;
import com.example.demo.student.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(StudentResponse::new)
                .collect(Collectors.toList());
    }

    public StudentResponse getStudentByPrn(String prn) {
        Student s = studentRepository.findByPrn(prn)
                .orElseThrow(() -> new StudentNotFoundException("PRN: " + prn));
        return new StudentResponse(s);
    }

    public StudentResponse getStudentById(String studentId) {
        return new StudentResponse(findOrThrow(studentId));
    }

    public Student getStudentModelById(String studentId) {
        return findOrThrow(studentId);
    }

    public void deleteStudent(String studentId) {
        studentRepository.delete(findOrThrow(studentId));
    }

    private Student findOrThrow(String studentId) {
        return studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));
    }
}
