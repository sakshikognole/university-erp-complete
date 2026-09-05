package com.example.demo.materials.repository;

import com.example.demo.materials.model.MaterialFolder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialFolderRepository extends MongoRepository<MaterialFolder, String> {

    Optional<MaterialFolder> findByFolderId(String folderId);

    // All root folders for a teacher (parentFolderId is null)
    List<MaterialFolder> findByTeacherIdAndParentFolderIdIsNull(String teacherId);

    // Sub-folders inside a parent folder for a teacher
    List<MaterialFolder> findByTeacherIdAndParentFolderId(String teacherId,
                                                           String parentFolderId);

    // All folders for a teacher (used for folder picker)
    List<MaterialFolder> findByTeacherId(String teacherId);

    boolean existsByFolderId(String folderId);
}
