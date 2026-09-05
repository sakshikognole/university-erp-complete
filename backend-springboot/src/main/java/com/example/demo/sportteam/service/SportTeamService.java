package com.example.demo.sportteam.service;

import com.example.demo.sportteam.model.RosterEntry;
import com.example.demo.sportteam.model.SportTeam;
import com.example.demo.sportteam.repository.SportTeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SportTeamService {

    private final SportTeamRepository sportTeamRepository;

    public SportTeamService(SportTeamRepository sportTeamRepository) {
        this.sportTeamRepository = sportTeamRepository;
    }

    // ── CRUD ─────────────────────────────────────────────────────────────────

    public List<SportTeam> getAll() {
        return sportTeamRepository.findAll();
    }

    public SportTeam getByTeamId(String teamId) {
        return sportTeamRepository.findByTeamId(teamId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Sport team not found with ID: " + teamId));
    }

    public SportTeam create(SportTeam team) {
        if (sportTeamRepository.existsByTeamId(team.getTeamId().trim())) {
            throw new IllegalArgumentException(
                    "Team ID '" + team.getTeamId() + "' already exists.");
        }
        team.setTeamId(team.getTeamId().trim());
        return sportTeamRepository.save(team);
    }

    public SportTeam update(String teamId, SportTeam incoming) {
        SportTeam existing = getByTeamId(teamId);
        existing.setSportId(incoming.getSportId());
        existing.setSportName(incoming.getSportName());
        existing.setCoachName(incoming.getCoachName());
        existing.setCaptainName(incoming.getCaptainName());
        return sportTeamRepository.save(existing);
    }

    public void delete(String teamId) {
        sportTeamRepository.delete(getByTeamId(teamId));
    }

    // ── Roster ───────────────────────────────────────────────────────────────

    public SportTeam addToRoster(String teamId, String studentPrn, String studentName) {
        SportTeam team = getByTeamId(teamId);
        boolean alreadyIn = team.getRoster().stream()
                .anyMatch(e -> e.getStudentPrn().equals(studentPrn));
        if (alreadyIn) {
            throw new IllegalArgumentException(
                    "Student with PRN " + studentPrn + " is already in this team's roster.");
        }
        team.getRoster().add(new RosterEntry(studentPrn, studentName));
        return sportTeamRepository.save(team);
    }

    public SportTeam removeFromRoster(String teamId, String studentPrn) {
        SportTeam team = getByTeamId(teamId);
        boolean removed = team.getRoster()
                .removeIf(e -> e.getStudentPrn().equals(studentPrn));
        if (!removed) {
            throw new IllegalArgumentException(
                    "Student with PRN " + studentPrn + " is not in this team's roster.");
        }
        return sportTeamRepository.save(team);
    }
}
