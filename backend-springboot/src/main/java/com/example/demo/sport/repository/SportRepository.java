package com.example.demo.sport.repository;

import com.example.demo.sport.model.Sport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SportRepository extends MongoRepository<Sport, String> {
    Optional<Sport> findBySportId(String sportId);
    boolean existsBySportId(String sportId);
}
