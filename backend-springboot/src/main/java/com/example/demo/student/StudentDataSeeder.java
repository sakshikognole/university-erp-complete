package com.example.demo.student;

import com.example.demo.student.model.LetterHead;
import com.example.demo.student.model.Student;
import com.example.demo.student.repository.LetterHeadRepository;
import com.example.demo.student.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(2)
public class StudentDataSeeder implements CommandLineRunner {

    private final StudentRepository    studentRepository;
    private final LetterHeadRepository letterHeadRepository;

    public StudentDataSeeder(StudentRepository studentRepository,
                             LetterHeadRepository letterHeadRepository) {
        this.studentRepository    = studentRepository;
        this.letterHeadRepository = letterHeadRepository;
    }

    @Override
    public void run(String... args) {
        seedStudents();
        seedLetterHead();
    }

    private void seedStudents() {
        List<Student> students = List.of(
            new Student("STU001", "Sakshi Kognole",  "Female", "2021001", "A", "2021", "Third Year",  "B.Tech Artificial Intelligence and Data Science", "2026-27"),
            new Student("STU002", "Rahul Patil",     "Male",   "2020002", "B", "2020", "Fourth Year", "B.Tech Computer Science and Engineering",         "2026-27"),
            new Student("STU003", "Priya Sharma",    "Female", "2022003", "A", "2022", "Second Year", "B.Tech Electronics and Communication Engineering","2026-27"),
            new Student("STU004", "Arjun Mehta",     "Male",   "2023004", "C", "2023", "First Year",  "B.Tech Mechanical Engineering",                   "2026-27"),
            new Student("STU005", "Neha Desai",      "Female", "2020005", "B", "2020", "Fourth Year", "B.Tech Information Technology",                   "2026-27")
        );
        int seeded = 0;
        for (Student s : students) {
            if (!studentRepository.existsByStudentId(s.getStudentId())) {
                studentRepository.save(s);
                seeded++;
            }
        }
        if (seeded > 0)
            System.out.println("[StudentDataSeeder] Seeded " + seeded + " student(s).");
        else
            System.out.println("[StudentDataSeeder] Students already present, skipping.");
    }

    private void seedLetterHead() {
        if (letterHeadRepository.findByKey("default").isPresent()) {
            System.out.println("[StudentDataSeeder] Letterhead already present, skipping.");
            return;
        }
        LetterHead lh = new LetterHead(
            "Your Trust / Management Name",
            "University ERP Institute of Technology",
            "123 College Road, City - 000000, State, India",
            "+91-0000-000000",
            "1800-000-0000",
            "+91-0000-000001",
            "www.university.edu",
            "contact@university.edu",
            "LOGO"
        );
        lh.setKey("default");
        letterHeadRepository.save(lh);
        System.out.println("[StudentDataSeeder] Default letterhead seeded.");
    }
}
