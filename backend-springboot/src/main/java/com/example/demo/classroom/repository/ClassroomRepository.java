package com.example.demo.classroom.repository;

import com.example.demo.classroom.model.Classroom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassroomRepository extends MongoRepository<Classroom, String> {
    Optional<Classroom> findByClassroomId(String classroomId);
    boolean existsByClassroomId(String classroomId);
}
