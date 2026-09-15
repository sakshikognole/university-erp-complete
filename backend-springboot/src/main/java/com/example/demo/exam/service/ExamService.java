package com.example.demo.exam.service;

import com.example.demo.exam.model.Exam;
import com.example.demo.exam.model.Exam.StudentMark;
import com.example.demo.exam.repository.ExamRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExamService {

    private final ExamRepository repo;

    public ExamService(ExamRepository repo) {
        this.repo = repo;
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    /**
     * T1/T2: maxMarks=20 → converted = raw/20*10, percentage = raw/20*100
     * SEE:   maxMarks=50 → converted = null,       percentage = raw/50*100
     */
    private List<StudentMark> compute(List<StudentMark> list, int maxMarks, boolean isT1T2) {
        if (list == null) return new ArrayList<>();
        list.forEach(m -> {
            if (m.getMarks() != null) {
                double raw = m.getMarks();
                m.setPercentage(round(raw / maxMarks * 100));
                m.setConverted(isT1T2 ? round(raw / maxMarks * 10) : null);
            } else {
                m.setConverted(null);
                m.setPercentage(null);
            }
        });
        return list;
    }

    private void computeAll(Exam exam) {
        exam.setT1Marks( compute(exam.getT1Marks(),  20, true));
        exam.setT2Marks( compute(exam.getT2Marks(),  20, true));
        exam.setSeeMarks(compute(exam.getSeeMarks(), 50, false));
    }

    // ── CRUD ──────────────────────────────────────────────────────────────

    public List<Exam> getAll() {
        return repo.findAllByOrderByExamIdAsc();
    }

    public Exam getByExamId(String examId) {
        return repo.findByExamId(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found: " + examId));
    }

    public Exam create(Exam exam) {
        String eid = exam.getExamId().trim();
        if (repo.existsByExamId(eid)) {
            throw new IllegalArgumentException("Exam ID \"" + eid + "\" already exists.");
        }
        exam.setExamId(eid);
        if (exam.getT1Marks()  == null) exam.setT1Marks(new ArrayList<>());
        if (exam.getT2Marks()  == null) exam.setT2Marks(new ArrayList<>());
        if (exam.getSeeMarks() == null) exam.setSeeMarks(new ArrayList<>());
        computeAll(exam);
        return repo.save(exam);
    }

    public Exam update(String examId, Exam incoming) {
        Exam existing = getByExamId(examId);
        existing.setSubject(incoming.getSubject());
        existing.setAcademicYear(incoming.getAcademicYear());
        existing.setDescription(incoming.getDescription());
        existing.setDisplayMode(
            incoming.getDisplayMode() != null ? incoming.getDisplayMode() : "PERCENTAGE"
        );
        existing.setPercentageScale(
            incoming.getPercentageScale() != null ? incoming.getPercentageScale() : 100
        );
        existing.setFractionScale(
            incoming.getFractionScale() != null ? incoming.getFractionScale() : 10
        );
        computeAll(existing);
        return repo.save(existing);
    }

    /** Save marks for one specific section: t1, t2, or see */
    public Exam saveMarksForSection(String examId, String section, List<StudentMark> marks) {
        Exam exam = getByExamId(examId);
        switch (section.toLowerCase()) {
            case "t1"  -> { exam.setT1Marks(marks);  compute(exam.getT1Marks(),  20, true); }
            case "t2"  -> { exam.setT2Marks(marks);  compute(exam.getT2Marks(),  20, true); }
            case "see" -> { exam.setSeeMarks(marks);  compute(exam.getSeeMarks(), 50, false); }
            default    -> throw new IllegalArgumentException("Unknown section: " + section + ". Use t1, t2, or see.");
        }
        return repo.save(exam);
    }

    public void delete(String examId) {
        repo.delete(getByExamId(examId));
    }
}
