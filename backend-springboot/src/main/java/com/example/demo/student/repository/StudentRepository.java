package com.example.demo.student.repository;

import com.example.demo.student.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {
    Optional<Student> findByStudentId(String studentId);
    boolean existsByStudentId(String studentId);

    // Used by Hostel and SportTeam PRN lookup
    Optional<Student> findByPrn(String prn);
}
