package com.example.demo.club.repository;

import com.example.demo.club.model.Club;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClubRepository extends MongoRepository<Club, String> {
    Optional<Club> findByClubId(String clubId);
    boolean existsByClubId(String clubId);
    List<Club> findByParentClubId(String parentClubId);
}
