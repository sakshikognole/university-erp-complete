package com.example.demo.sport.repository;

import com.example.demo.sport.model.SportTeam;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("legacySportTeamRepository")
public interface SportTeamRepository extends MongoRepository<SportTeam, String> {
    Optional<SportTeam> findByTeamId(String teamId);
    boolean existsByTeamId(String teamId);
    List<SportTeam> findBySportId(String sportId);
}
