package com.example.demo.sportteam.repository;

import com.example.demo.sportteam.model.TeamRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRequestRepository extends MongoRepository<TeamRequest, String> {
    Optional<TeamRequest> findByRequestId(String requestId);
    List<TeamRequest> findByTeamId(String teamId);
    boolean existsByTeamIdAndStudentPrnAndStatus(String teamId, String studentPrn, String status);
}
