package com.example.demo.sport.service;

import com.example.demo.sport.model.SportTeam;
import com.example.demo.sport.repository.SportTeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("legacySportTeamService")
public class SportTeamService {

    private final SportTeamRepository repo;

    public SportTeamService(SportTeamRepository repo) {
        this.repo = repo;
    }

    public List<SportTeam> getAll() {
        return repo.findAll();
    }

    public List<SportTeam> getBySportId(String sportId) {
        return repo.findBySportId(sportId);
    }

    public SportTeam getByTeamId(String teamId) {
        return repo.findByTeamId(teamId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Team not found with ID: " + teamId));
    }

    public SportTeam create(SportTeam team) {
        String tid = team.getTeamId().trim();
        if (repo.existsByTeamId(tid)) {
            throw new IllegalArgumentException(
                    "Team ID '" + tid + "' already exists.");
        }
        team.setTeamId(tid);
        return repo.save(team);
    }

    public SportTeam update(String teamId, SportTeam incoming) {
        SportTeam existing = getByTeamId(teamId);
        existing.setTeamName(incoming.getTeamName());
        existing.setSportId(incoming.getSportId());
        existing.setCoachName(incoming.getCoachName());
        existing.setMembers(incoming.getMembers());
        existing.setStatus(incoming.getStatus());
        existing.setDescription(incoming.getDescription());
        return repo.save(existing);
    }

    public void delete(String teamId) {
        repo.delete(getByTeamId(teamId));
    }
}
