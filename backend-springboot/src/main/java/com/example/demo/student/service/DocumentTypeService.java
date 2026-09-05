package com.example.demo.student.service;

import com.example.demo.student.model.DocumentType;
import com.example.demo.student.repository.DocumentTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentTypeService {

    private final DocumentTypeRepository documentTypeRepository;

    public DocumentTypeService(DocumentTypeRepository documentTypeRepository) {
        this.documentTypeRepository = documentTypeRepository;
    }

    public List<DocumentType> getAllDocumentTypes() {
        return documentTypeRepository.findAll();
    }

    public DocumentType addDocumentType(String documentName, String defaultContent) {
        if (documentTypeRepository.existsByDocumentName(documentName.trim())) {
            throw new IllegalArgumentException(
                "Document type '" + documentName + "' already exists.");
        }
        return documentTypeRepository.save(
            new DocumentType(documentName.trim(), defaultContent));
    }

    public DocumentType updateContent(String id, String defaultContent) {
        DocumentType doc = documentTypeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Document type not found."));
        doc.setDefaultContent(defaultContent);
        return documentTypeRepository.save(doc);
    }

    public void deleteDocumentType(String id) {
        documentTypeRepository.deleteById(id);
    }
}
