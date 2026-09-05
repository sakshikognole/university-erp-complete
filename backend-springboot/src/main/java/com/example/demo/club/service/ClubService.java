package com.example.demo.club.service;

import com.example.demo.club.model.Club;
import com.example.demo.club.repository.ClubRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubService {

    private final ClubRepository clubRepository;

    public ClubService(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    public List<Club> getAll() {
        return clubRepository.findAll();
    }

    public Club create(Club club) {
        if (club.getClubId() == null || club.getClubId().isBlank()) {
            throw new IllegalArgumentException("Club ID is required.");
        }
        if (clubRepository.existsByClubId(club.getClubId().trim())) {
            throw new IllegalArgumentException("Club ID '" + club.getClubId() + "' already exists.");
        }
        // Treat empty parentClubId as null (independent club)
        if (club.getParentClubId() != null && club.getParentClubId().isBlank()) {
            club.setParentClubId(null);
        }
        club.setClubId(club.getClubId().trim());
        return clubRepository.save(club);
    }

    public Club update(String id, Club incoming) {
        Club existing = clubRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Club not found."));

        existing.setClubName(incoming.getClubName());
        existing.setClubCategory(incoming.getClubCategory());
        existing.setDescription(incoming.getDescription());
        existing.setFacultyCoordinator(incoming.getFacultyCoordinator());
        existing.setStudentLeadName(incoming.getStudentLeadName());
        existing.setStudentLeadId(incoming.getStudentLeadId());
        existing.setStudentLeadRole(incoming.getStudentLeadRole());
        existing.setActiveMembers(incoming.getActiveMembers());
        existing.setStatus(incoming.getStatus());

        // Update parentClubId — empty string means independent
        String parent = incoming.getParentClubId();
        existing.setParentClubId((parent != null && !parent.isBlank()) ? parent.trim() : null);

        return clubRepository.save(existing);
    }

    public void delete(String id) {
        clubRepository.deleteById(id);
    }
}
