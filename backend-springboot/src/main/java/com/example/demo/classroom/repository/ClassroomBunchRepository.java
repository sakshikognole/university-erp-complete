package com.example.demo.classroom.repository;

import com.example.demo.classroom.model.ClassroomBunch;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassroomBunchRepository extends MongoRepository<ClassroomBunch, String> {
    ClassroomBunch findByBunchId(String bunchId);
}
