package com.example.demo.materials.service;

import com.example.demo.materials.model.MaterialFolder;
import com.example.demo.materials.repository.MaterialFolderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class MaterialFolderService {

    private final MaterialFolderRepository folderRepository;

    public MaterialFolderService(MaterialFolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    // ── Queries ──────────────────────────────────────────────────────────────

    /** Root-level folders for a teacher. */
    public List<MaterialFolder> getRootFolders(String teacherId) {
        return folderRepository.findByTeacherIdAndParentFolderIdIsNull(teacherId);
    }

    /** Sub-folders inside a parent for a teacher. */
    public List<MaterialFolder> getSubFolders(String teacherId, String parentFolderId) {
        return folderRepository.findByTeacherIdAndParentFolderId(teacherId, parentFolderId);
    }

    /** All folders for a teacher (flat list — used in folder picker). */
    public List<MaterialFolder> getAllFolders(String teacherId) {
        return folderRepository.findByTeacherId(teacherId);
    }

    /** All folders regardless of teacher (student view). */
    public List<MaterialFolder> getAllFolders() {
        return folderRepository.findAll();
    }

    public MaterialFolder getByFolderId(String folderId) {
        return folderRepository.findByFolderId(folderId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Folder not found: " + folderId));
    }

    // ── Commands ─────────────────────────────────────────────────────────────

    public MaterialFolder create(String folderName, String teacherId,
                                 String parentFolderId) {
        if (folderName == null || folderName.isBlank()) {
            throw new IllegalArgumentException("Folder name is required.");
        }
        if (teacherId == null || teacherId.isBlank()) {
            throw new IllegalArgumentException("Teacher ID is required.");
        }
        MaterialFolder folder = new MaterialFolder();
        folder.setFolderId("FLD-" + UUID.randomUUID()
                .toString().substring(0, 8).toUpperCase());
        folder.setFolderName(folderName.trim());
        folder.setTeacherId(teacherId);
        folder.setParentFolderId(
                (parentFolderId == null || parentFolderId.isBlank())
                        ? null : parentFolderId);
        folder.setCreatedDate(LocalDateTime.now());
        folder.setUpdatedDate(LocalDateTime.now());
        return folderRepository.save(folder);
    }

    public MaterialFolder rename(String folderId, String newName, String teacherId) {
        MaterialFolder folder = getByFolderId(folderId);
        enforceOwnership(folder, teacherId);
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Folder name is required.");
        }
        folder.setFolderName(newName.trim());
        folder.setUpdatedDate(LocalDateTime.now());
        return folderRepository.save(folder);
    }

    public void delete(String folderId, String teacherId) {
        MaterialFolder folder = getByFolderId(folderId);
        enforceOwnership(folder, teacherId);
        folderRepository.delete(folder);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void enforceOwnership(MaterialFolder folder, String teacherId) {
        if (!folder.getTeacherId().equals(teacherId)) {
            throw new SecurityException(
                    "You do not have permission to modify this folder.");
        }
    }
}
