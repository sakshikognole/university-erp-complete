package com.example.demo.subject.repository;

import com.example.demo.subject.model.Subject;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends MongoRepository<Subject, String> {
    Optional<Subject> findBySubjectId(String subjectId);
    boolean existsBySubjectId(String subjectId);
    // Used for auto-generating the next SUB-NNN id
    List<Subject> findAllByOrderBySubjectIdAsc();
}
