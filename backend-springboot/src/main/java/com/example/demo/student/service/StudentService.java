package com.example.demo.student.service;

import com.example.demo.student.exception.StudentNotFoundException;
import com.example.demo.student.model.Student;
import com.example.demo.student.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Page<Student> getAllStudents(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    public List<Student> getAllStudentsList() {
        return studentRepository.findAll();
    }

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student getStudentById(String studentId) {
        return findOrThrow(studentId);
    }

    public Student getStudentModelById(String studentId) {
        return findOrThrow(studentId);
    }

    public void deleteStudent(String studentId) {
        studentRepository.delete(findOrThrow(studentId));
    }

    private Student findOrThrow(String studentId) {
        if (studentId == null || studentId.isBlank()) {
            throw new StudentNotFoundException("Student ID cannot be null or empty");
        }
        return studentRepository.findById(studentId)
                .or(() -> studentRepository.findByStudentId(studentId))
                .or(() -> studentRepository.findByPrn(studentId))
                .or(() -> studentRepository.findByPrnIgnoreCase(studentId))
                .orElseThrow(() -> new StudentNotFoundException(studentId));
    }
}
