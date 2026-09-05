package com.example.demo.club;

import com.example.demo.club.model.Club;
import com.example.demo.club.repository.ClubRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(3)
public class ClubDataSeeder implements CommandLineRunner {

    private final ClubRepository clubRepository;

    public ClubDataSeeder(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    @Override
    public void run(String... args) {
        List<Club> clubs = buildClubs();
        int seeded = 0;
        for (Club c : clubs) {
            if (!clubRepository.existsByClubId(c.getClubId())) {
                clubRepository.save(c);
                seeded++;
            }
        }
        if (seeded > 0)
            System.out.println("[ClubDataSeeder] Seeded " + seeded + " club(s).");
        else
            System.out.println("[ClubDataSeeder] Clubs already present, skipping.");
    }

    private List<Club> buildClubs() {
        // ── Independent / Parent Clubs ──────────────────────────────

        Club coding = club(
            "CLB001", "Coding Club", "Technical", null,
            "A community for programming enthusiasts to learn, build, and compete.",
            "Prof. R. Sharma", "Rahul Patil", "STU002", "President",
            120, "Active"
        );

        Club sports = club(
            "CLB002", "Sports Club", "Sports", null,
            "Promotes physical fitness and sportsmanship across all disciplines.",
            "Prof. M. Verma", "Arjun Mehta", "STU004", "Captain",
            85, "Active"
        );

        Club music = club(
            "CLB003", "Music Club", "Cultural", null,
            "Celebrates musical talent through performances, workshops, and competitions.",
            "Prof. S. Nair", "Neha Desai", "STU005", "Club Lead",
            60, "Active"
        );

        Club robotics = club(
            "CLB004", "Robotics Club", "Technical", null,
            "Builds and programs robots for competitions and real-world problem solving.",
            "Prof. A. Kulkarni", "Sakshi Kognole", "STU001", "President",
            45, "Active"
        );

        Club drama = club(
            "CLB005", "Drama Club", "Cultural", null,
            "Brings stories to life through theatre, acting workshops, and annual plays.",
            "Prof. P. Joshi", "Priya Sharma", "STU003", "Director",
            40, "Active"
        );

        Club photography = club(
            "CLB006", "Photography Club", "Arts", null,
            "Explores the art of photography through exhibitions and field trips.",
            "Prof. D. Rao", "Rahul Patil", "STU002", "Club Lead",
            35, "Inactive"
        );

        // ── Sub-clubs under Coding Club (CLB001) ────────────────────

        Club cpp = club(
            "CLB001-A", "C++ Coding", "Technical", "CLB001",
            "Focused on competitive programming and system-level development using C++.",
            "Prof. R. Sharma", "Arjun Mehta", "STU004", "Lead",
            38, "Active"
        );

        Club java = club(
            "CLB001-B", "Java Coding", "Technical", "CLB001",
            "Covers Java development, Spring Boot, and enterprise application building.",
            "Prof. R. Sharma", "Sakshi Kognole", "STU001", "Lead",
            42, "Active"
        );

        Club python = club(
            "CLB001-C", "Python Coding", "Technical", "CLB001",
            "Explores Python for data science, automation, and web development.",
            "Prof. R. Sharma", "Neha Desai", "STU005", "Lead",
            40, "Active"
        );

        // ── Sub-clubs under Sports Club (CLB002) ────────────────────

        Club cricket = club(
            "CLB002-A", "Cricket Team", "Sports", "CLB002",
            "University cricket team participating in inter-college tournaments.",
            "Prof. M. Verma", "Arjun Mehta", "STU004", "Captain",
            22, "Active"
        );

        Club basketball = club(
            "CLB002-B", "Basketball Team", "Sports", "CLB002",
            "University basketball team for men and women.",
            "Prof. M. Verma", "Rahul Patil", "STU002", "Captain",
            18, "Active"
        );

        // ── Sub-clubs under Music Club (CLB003) ─────────────────────

        Club classical = club(
            "CLB003-A", "Classical Music", "Cultural", "CLB003",
            "Dedicated to classical Indian music — vocal and instrumental.",
            "Prof. S. Nair", "Priya Sharma", "STU003", "Lead",
            20, "Active"
        );

        Club western = club(
            "CLB003-B", "Western Music", "Cultural", "CLB003",
            "Covers guitar, drums, bass, and Western vocal performances.",
            "Prof. S. Nair", "Neha Desai", "STU005", "Lead",
            22, "Active"
        );

        return List.of(
            coding, sports, music, robotics, drama, photography,
            cpp, java, python,
            cricket, basketball,
            classical, western
        );
    }

    private Club club(String clubId, String clubName, String category,
                      String parentClubId, String description,
                      String faculty, String leadName, String leadId,
                      String leadRole, int members, String status) {
        Club c = new Club();
        c.setClubId(clubId);
        c.setClubName(clubName);
        c.setClubCategory(category);
        c.setParentClubId(parentClubId);
        c.setDescription(description);
        c.setFacultyCoordinator(faculty);
        c.setStudentLeadName(leadName);
        c.setStudentLeadId(leadId);
        c.setStudentLeadRole(leadRole);
        c.setActiveMembers(members);
        c.setStatus(status);
        return c;
    }
}
