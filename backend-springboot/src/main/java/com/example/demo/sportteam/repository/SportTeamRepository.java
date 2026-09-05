package com.example.demo.sportteam.repository;

import com.example.demo.sportteam.model.SportTeam;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SportTeamRepository extends MongoRepository<SportTeam, String> {
    Optional<SportTeam> findByTeamId(String teamId);
    boolean existsByTeamId(String teamId);
}
