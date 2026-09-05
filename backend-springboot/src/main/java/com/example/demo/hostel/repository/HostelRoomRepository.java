package com.example.demo.hostel.repository;

import com.example.demo.hostel.model.HostelRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HostelRoomRepository extends MongoRepository<HostelRoom, String> {
    Optional<HostelRoom> findByRoomId(String roomId);
    List<HostelRoom>     findByBlockId(String blockId);
    boolean existsByBlockIdAndRoomNo(String blockId, String roomNo);
}
