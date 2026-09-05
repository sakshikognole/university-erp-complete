package com.example.demo.materials.service;

import com.example.demo.materials.model.Material;
import com.example.demo.materials.repository.MaterialRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class MaterialService {

    private static final Set<String> ALLOWED_EXTENSIONS =
            Set.of("pdf", "jpg", "jpeg", "png", "xls", "xlsx");

    private static final long MAX_FILE_SIZE = 20L * 1024 * 1024; // 20 MB

    @Value("${app.upload.dir:uploads/study-materials}")
    private String uploadDir;

    private Path uploadPath;

    private final MaterialRepository materialRepository;

    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    /**
     * Runs once after Spring injects @Value fields.
     * Converts the (possibly relative) uploadDir to an absolute path
     * so files are always found regardless of working directory.
     */
    @PostConstruct
    public void init() throws IOException {
        uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);
        System.out.println("[MaterialService] Upload directory: " + uploadPath);
    }

    // ── Queries ──────────────────────────────────────────────────────────────

    public Material getByMaterialId(String materialId) {
        return materialRepository.findByMaterialId(materialId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Material not found: " + materialId));
    }

    public List<Material> getByFolder(String teacherId, String folderId) {
        return materialRepository.findByTeacherIdAndFolderId(teacherId, folderId);
    }

    public List<Material> getAllForTeacher(String teacherId) {
        return materialRepository.findByTeacherId(teacherId);
    }

    public List<Material> getByFolderForStudent(String folderId) {
        return materialRepository.findByFolderId(folderId);
    }

    public List<Material> searchAll(String name) {
        if (name == null || name.isBlank()) return materialRepository.findAll();
        return materialRepository.findByFileNameContainingIgnoreCase(name);
    }

    public List<Material> searchForTeacher(String teacherId, String name) {
        if (name == null || name.isBlank())
            return materialRepository.findByTeacherId(teacherId);
        return materialRepository
                .findByTeacherIdAndFileNameContainingIgnoreCase(teacherId, name);
    }

    // ── Upload ───────────────────────────────────────────────────────────────

    public Material upload(MultipartFile file, String folderId,
                           String teacherId) throws IOException {
        if (file == null || file.isEmpty())
            throw new IllegalArgumentException("File is required.");
        if (folderId == null || folderId.isBlank())
            throw new IllegalArgumentException("Folder is required.");
        if (teacherId == null || teacherId.isBlank())
            throw new IllegalArgumentException("Teacher ID is required.");

        String originalName = file.getOriginalFilename() != null
                ? file.getOriginalFilename() : "file";
        String ext = getExtension(originalName).toLowerCase();

        if (!ALLOWED_EXTENSIONS.contains(ext))
            throw new IllegalArgumentException(
                    "File type ." + ext + " is not supported. "
                    + "Allowed: PDF, JPG, JPEG, PNG, XLS, XLSX.");

        if (file.getSize() > MAX_FILE_SIZE)
            throw new IllegalArgumentException("File size exceeds the 20 MB limit.");

        // Save to  uploadPath / teacherId / uuid.ext
        Path teacherDir = uploadPath.resolve(teacherId);
        Files.createDirectories(teacherDir);

        String storedName = UUID.randomUUID() + "." + ext;
        Path dest = teacherDir.resolve(storedName);
        Files.copy(file.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);

        System.out.println("[MaterialService] Saved file: " + dest.toAbsolutePath());

        String materialId = "MAT-" + UUID.randomUUID()
                .toString().substring(0, 8).toUpperCase();

        Material material = new Material();
        material.setMaterialId(materialId);
        material.setFileName(originalName);
        material.setStoredFileName(storedName);
        material.setFileType(ext.toUpperCase());
        material.setFileSize(file.getSize());
        material.setTeacherId(teacherId);
        material.setFolderId(folderId);
        material.setFileUrl("/api/materials/" + materialId + "/download");
        material.setUploadedDate(LocalDateTime.now());
        material.setUpdatedDate(LocalDateTime.now());

        return materialRepository.save(material);
    }

    // ── Rename ───────────────────────────────────────────────────────────────

    public Material rename(String materialId, String newFileName, String teacherId) {
        Material material = getByMaterialId(materialId);
        enforceOwnership(material, teacherId);
        if (newFileName == null || newFileName.isBlank())
            throw new IllegalArgumentException("File name is required.");
        material.setFileName(newFileName.trim());
        material.setUpdatedDate(LocalDateTime.now());
        return materialRepository.save(material);
    }

    // ── Delete ───────────────────────────────────────────────────────────────

    public void delete(String materialId, String teacherId) throws IOException {
        Material material = getByMaterialId(materialId);
        enforceOwnership(material, teacherId);
        Path filePath = uploadPath.resolve(material.getTeacherId())
                .resolve(material.getStoredFileName());
        Files.deleteIfExists(filePath);
        materialRepository.delete(material);
    }

    // ── Download ─────────────────────────────────────────────────────────────

    public Path resolveFilePath(String materialId) {
        Material material = getByMaterialId(materialId);
        return uploadPath.resolve(material.getTeacherId())
                .resolve(material.getStoredFileName());
    }

    public Material getMeta(String materialId) {
        return getByMaterialId(materialId);
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private void enforceOwnership(Material material, String teacherId) {
        if (!material.getTeacherId().equals(teacherId))
            throw new SecurityException(
                    "You do not have permission to modify this file.");
    }

    private String getExtension(String filename) {
        int dot = filename.lastIndexOf('.');
        return (dot >= 0 && dot < filename.length() - 1)
                ? filename.substring(dot + 1) : "";
    }
}
