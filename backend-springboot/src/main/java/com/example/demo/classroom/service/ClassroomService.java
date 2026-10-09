package com.example.demo.classroom.service;

import com.example.demo.classroom.model.Classroom;
import com.example.demo.classroom.repository.ClassroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClassroomService {

    @Autowired
    private ClassroomRepository repository;

    // ── Get all with pagination ────────────────────────────────────────────
    public Page<Classroom> getAllClassrooms(Pageable pageable) {
        return repository.findAll(pageable);
    }

    // ── Get by ID ──────────────────────────────────────────────────────────
    public Classroom getByClassroomId(String classroomId) {
        return repository.findByClassroomId(classroomId)
            .orElseThrow(() -> new RuntimeException("Classroom not found: " + classroomId));
    }

    // ── Create ─────────────────────────────────────────────────────────────
    public Classroom createClassroom(Classroom classroom) {
        // Auto-generate classroom ID
        classroom.setClassroomId(generateClassroomId());
        
        // Calculate capacity
        int capacity = (classroom.getRows() * classroom.getColumns()) + classroom.getExtra();
        classroom.setCapacity(capacity);
        
        return repository.save(classroom);
    }

    // ── Update ─────────────────────────────────────────────────────────────
    public Classroom updateClassroom(String classroomId, Classroom updated) {
        Classroom existing = getByClassroomId(classroomId);
        
        existing.setClassroomName(updated.getClassroomName());
        existing.setRows(updated.getRows());
        existing.setColumns(updated.getColumns());
        existing.setExtra(updated.getExtra());
        existing.setExaminerId(updated.getExaminerId());
        existing.setExaminerName(updated.getExaminerName());
        
        // Recalculate capacity
        int capacity = (existing.getRows() * existing.getColumns()) + existing.getExtra();
        existing.setCapacity(capacity);
        
        return repository.save(existing);
    }

    // ── Delete ─────────────────────────────────────────────────────────────
    public void deleteClassroom(String classroomId) {
        Classroom classroom = getByClassroomId(classroomId);
        repository.delete(classroom);
    }

    // ── Generate ID: CLS-001, CLS-002, ... ────────────────────────────────
    private String generateClassroomId() {
        long count = repository.count();
        return String.format("CLS-%03d", count + 1);
    }
}
