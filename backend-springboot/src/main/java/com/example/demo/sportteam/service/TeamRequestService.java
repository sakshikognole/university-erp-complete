package com.example.demo.sportteam.service;

import com.example.demo.sportteam.model.TeamRequest;
import com.example.demo.sportteam.repository.TeamRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TeamRequestService {

    private final TeamRequestRepository teamRequestRepository;
    private final SportTeamService sportTeamService;

    public TeamRequestService(TeamRequestRepository teamRequestRepository,
                              SportTeamService sportTeamService) {
        this.teamRequestRepository = teamRequestRepository;
        this.sportTeamService      = sportTeamService;
    }

    public List<TeamRequest> getByTeamId(String teamId) {
        return teamRequestRepository.findByTeamId(teamId);
    }

    public TeamRequest create(String teamId, String studentPrn, String studentName) {
        // Verify team exists
        sportTeamService.getByTeamId(teamId);

        // Prevent duplicate PENDING request from same student for same team
        if (teamRequestRepository.existsByTeamIdAndStudentPrnAndStatus(
                teamId, studentPrn, "PENDING")) {
            throw new IllegalArgumentException(
                    "A pending request from PRN " + studentPrn + " already exists for this team.");
        }

        TeamRequest req = new TeamRequest();
        req.setRequestId("REQ-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        req.setTeamId(teamId);
        req.setStudentPrn(studentPrn);
        req.setStudentName(studentName);
        req.setStatus("PENDING");
        return teamRequestRepository.save(req);
    }

    public TeamRequest accept(String requestId) {
        TeamRequest req = findOrThrow(requestId);
        req.setStatus("ACCEPTED");
        teamRequestRepository.save(req);

        // Add student to roster (service handles duplicate check internally)
        try {
            sportTeamService.addToRoster(req.getTeamId(), req.getStudentPrn(), req.getStudentName());
        } catch (IllegalArgumentException ex) {
            // Student already in roster — that's fine, just update the request status
        }
        return req;
    }

    public TeamRequest ignore(String requestId) {
        TeamRequest req = findOrThrow(requestId);
        req.setStatus("IGNORED");
        return teamRequestRepository.save(req);
    }

    private TeamRequest findOrThrow(String requestId) {
        return teamRequestRepository.findByRequestId(requestId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Request not found with ID: " + requestId));
    }
}
