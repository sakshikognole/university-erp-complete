package com.example.demo.student.repository;

import com.example.demo.student.model.LetterHead;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LetterHeadRepository extends MongoRepository<LetterHead, String> {
    Optional<LetterHead> findByKey(String key);
}
