package com.example.demo.student.repository;

import com.example.demo.student.model.DocumentType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentTypeRepository extends MongoRepository<DocumentType, String> {
    Optional<DocumentType> findByDocumentName(String documentName);
    boolean existsByDocumentName(String documentName);
}
