package com.example.demo.schedule.repository;

import com.example.demo.schedule.model.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScheduleRepository extends MongoRepository<Schedule, String> {
    Optional<Schedule> findByScheduleId(String scheduleId);
    boolean existsByScheduleId(String scheduleId);
    Page<Schedule> findAll(Pageable pageable);
}
