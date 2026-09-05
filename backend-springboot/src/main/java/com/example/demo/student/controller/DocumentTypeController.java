package com.example.demo.student.controller;

import com.example.demo.student.model.DocumentType;
import com.example.demo.student.service.DocumentTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/document-types")
public class DocumentTypeController {

    private final DocumentTypeService documentTypeService;

    public DocumentTypeController(DocumentTypeService documentTypeService) {
        this.documentTypeService = documentTypeService;
    }

    @GetMapping
    public ResponseEntity<List<DocumentType>> getAll() {
        return ResponseEntity.ok(documentTypeService.getAllDocumentTypes());
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Map<String, String> body) {
        String name    = body.getOrDefault("documentName",   "").trim();
        String content = body.getOrDefault("defaultContent", "").trim();
        if (name.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Document name is required."));
        }
        try {
            return ResponseEntity.ok(documentTypeService.addDocumentType(name, content));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @PutMapping("/{id}/content")
    public ResponseEntity<?> updateContent(@PathVariable String id,
                                           @RequestBody Map<String, String> body) {
        String content = body.getOrDefault("defaultContent", "");
        try {
            return ResponseEntity.ok(documentTypeService.updateContent(id, content));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        documentTypeService.deleteDocumentType(id);
        return ResponseEntity.ok(Map.of("message", "Document type deleted."));
    }
}
