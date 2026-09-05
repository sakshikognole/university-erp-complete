package com.example.demo.materials.repository;

import com.example.demo.materials.model.Material;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialRepository extends MongoRepository<Material, String> {

    Optional<Material> findByMaterialId(String materialId);

    // Files inside a specific folder for a teacher
    List<Material> findByTeacherIdAndFolderId(String teacherId, String folderId);

    // All files for a teacher (search / flat list)
    List<Material> findByTeacherId(String teacherId);

    // All files in a folder regardless of teacher (student view)
    List<Material> findByFolderId(String folderId);

    // All files (student search)
    List<Material> findByFileNameContainingIgnoreCase(String name);

    // Teacher-scoped search
    List<Material> findByTeacherIdAndFileNameContainingIgnoreCase(String teacherId,
                                                                   String name);
}
