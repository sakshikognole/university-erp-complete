package com.example.demo.subject.service;

import com.example.demo.subject.model.Subject;
import com.example.demo.subject.repository.SubjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    // ── GET ALL ───────────────────────────────────────────────────────────
    public List<Subject> getAll() {
        return subjectRepository.findAllByOrderBySubjectIdAsc();
    }

    // ── GET ONE ───────────────────────────────────────────────────────────
    public Subject getBySubjectId(String subjectId) {
        return subjectRepository.findBySubjectId(subjectId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Subject not found with ID: " + subjectId));
    }

    // ── Repository helper ─────────────────────────────────────────────────
    // Needed for duplicate name check
    private boolean existsBySubjectNameIgnoreCase(String name) {
        return subjectRepository.findAll().stream()
                .anyMatch(s -> s.getSubjectName() != null &&
                               s.getSubjectName().trim().equalsIgnoreCase(name.trim()));
    }

    private boolean existsBySubjectNameIgnoreCaseExcluding(String name, String excludeSubjectId) {
        return subjectRepository.findAll().stream()
                .filter(s -> !s.getSubjectId().equals(excludeSubjectId))
                .anyMatch(s -> s.getSubjectName() != null &&
                               s.getSubjectName().trim().equalsIgnoreCase(name.trim()));
    }

    // ── CREATE ────────────────────────────────────────────────────────────
    // Subject ID is AUTO-GENERATED as SUB-001, SUB-002, ...
    public Subject create(Subject subject) {
        if (existsBySubjectNameIgnoreCase(subject.getSubjectName())) {
            throw new IllegalArgumentException(
                "Subject \"" + subject.getSubjectName().trim() + "\" already exists.");
        }
        subject.setSubjectId(generateNextId());
        return subjectRepository.save(subject);
    }

    // ── UPDATE ────────────────────────────────────────────────────────────
    public Subject update(String subjectId, Subject incoming) {
        Subject existing = getBySubjectId(subjectId);
        // Duplicate name check — exclude the current subject itself
        if (existsBySubjectNameIgnoreCaseExcluding(incoming.getSubjectName(), subjectId)) {
            throw new IllegalArgumentException(
                "Subject \"" + incoming.getSubjectName().trim() + "\" already exists.");
        }
        existing.setSubjectName(incoming.getSubjectName());
        existing.setDescription(incoming.getDescription());
        return subjectRepository.save(existing);
    }

    // ── DELETE ────────────────────────────────────────────────────────────
    public void delete(String subjectId) {
        subjectRepository.delete(getBySubjectId(subjectId));
    }

    // ── AUTO-ID GENERATOR ─────────────────────────────────────────────────
    // Finds the current max SUB-NNN number and increments by 1.
    // Falls back to SUB-001 if no subjects exist yet.
    private String generateNextId() {
        List<Subject> all = subjectRepository.findAll();
        int max = 0;
        for (Subject s : all) {
            String sid = s.getSubjectId();
            if (sid != null && sid.startsWith("SUB-")) {
                try {
                    int n = Integer.parseInt(sid.substring(4));
                    if (n > max) max = n;
                } catch (NumberFormatException ignored) {}
            }
        }
        return String.format("SUB-%03d", max + 1);
    }
}
