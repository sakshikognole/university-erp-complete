package com.example.demo.exam.repository;

import com.example.demo.exam.model.Exam;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamRepository extends MongoRepository<Exam, String> {
    Optional<Exam> findByExamId(String examId);
    boolean existsByExamId(String examId);
    List<Exam> findAllByOrderByExamIdAsc();
    List<Exam> findBySubjectContainingIgnoreCase(String subject);
}
