package com.example.demo.exam.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * One Exam document = one subject.
 * Marks for T1, T2 and SEE are stored in three separate lists
 * so each section is fully isolated.
 *
 * T1/T2 maxMarks = 20  →  raw marks converted to /10 + percentage
 * SEE   maxMarks = 50  →  percentage only
 */
@Document(collection = "exams")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Exam {

    @Id
    private String id;

    @Indexed(unique = true)
    @NotBlank(message = "Exam ID is required")
    private String examId;

    @NotBlank(message = "Subject name is required")
    private String subject;

    private String academicYear;
    private String description;

    private String displayMode = "PERCENTAGE";

    /** Custom max % for PERCENTAGE mode. e.g. 50 means show as x% out of 50. Default: 100 */
    private Integer percentageScale = 100;

    /** Custom denominator for FRACTION mode. e.g. 5 means show as x/5. Default: 10 */
    private Integer fractionScale = 10;

    // ── Isolated marks lists per exam type ────────────────────────────────
    private List<StudentMark> t1Marks  = new ArrayList<>();
    private List<StudentMark> t2Marks  = new ArrayList<>();
    private List<StudentMark> seeMarks = new ArrayList<>();

    // ── Embedded student mark ─────────────────────────────────────────────
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudentMark {
        private int    serialNo;
        private String prn;
        private String studentName;

        /** Raw marks entered by user */
        private Double marks;

        /** Computed by service — only for T1/T2 (/10 scale) */
        private Double converted;

        /** Computed by service — percentage out of maxMarks */
        private Double percentage;
    }
}
