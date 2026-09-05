package com.example.demo.hostel.repository;

import com.example.demo.hostel.model.HostelBlock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HostelBlockRepository extends MongoRepository<HostelBlock, String> {
    Optional<HostelBlock> findByBlockId(String blockId);
    boolean existsByBlockId(String blockId);
    Page<HostelBlock> findAll(Pageable pageable);
}
