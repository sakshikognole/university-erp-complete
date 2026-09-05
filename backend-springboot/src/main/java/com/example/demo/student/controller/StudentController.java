package com.example.demo.student.controller;

import com.example.demo.student.dto.StudentResponse;
import com.example.demo.student.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAll() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/by-prn/{prn}")
    public ResponseEntity<?> getByPrn(@PathVariable String prn) {
        try {
            return ResponseEntity.ok(studentService.getStudentByPrn(prn));
        } catch (Exception ex) {
            return ResponseEntity.status(404).body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<StudentResponse> getOne(@PathVariable String studentId) {
        return ResponseEntity.ok(studentService.getStudentById(studentId));
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<?> delete(@PathVariable String studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.ok(Map.of("message", "Student deleted successfully."));
    }
}
