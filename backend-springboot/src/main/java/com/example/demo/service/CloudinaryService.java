package com.example.demo.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    // Upload image to Cloudinary
    public String uploadImage(MultipartFile file, String folder) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("File is empty");
        }

        // Validate file type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IOException("File must be an image");
        }

        // Upload to Cloudinary
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
            ObjectUtils.asMap(
                "folder", folder,
                "resource_type", "image",
                "transformation", ObjectUtils.asMap(
                    "quality", "auto",
                    "fetch_format", "auto"
                )
            )
        );

        // Return the secure URL
        return (String) uploadResult.get("secure_url");
    }

    // Upload image with base64 string
    public String uploadImageBase64(String base64Data, String folder) throws IOException {
        if (base64Data == null || base64Data.trim().isEmpty()) {
            throw new IOException("Base64 data is empty");
        }

        // Upload to Cloudinary
        Map uploadResult = cloudinary.uploader().upload(base64Data,
            ObjectUtils.asMap(
                "folder", folder,
                "resource_type", "image",
                "transformation", ObjectUtils.asMap(
                    "quality", "auto",
                    "fetch_format", "auto"
                )
            )
        );

        // Return the secure URL
        return (String) uploadResult.get("secure_url");
    }

    // Delete image from Cloudinary
    public void deleteImage(String imageUrl) throws IOException {
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            return;
        }

        try {
            // Extract public ID from URL
            String publicId = extractPublicId(imageUrl);

            if (publicId != null && !publicId.isEmpty()) {
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            }
        } catch (Exception e) {
            // Log error but don't throw - deletion failure shouldn't block operations
            System.err.println("Failed to delete image from Cloudinary: " + e.getMessage());
        }
    }

    // Extract public ID from Cloudinary URL
    private String extractPublicId(String imageUrl) {
        if (imageUrl == null || !imageUrl.contains("cloudinary.com")) {
            return null;
        }

        try {
            // URL format: https://res.cloudinary.com/{cloud_name}/image/upload/{transformations}/{public_id}.{format}
            String[] parts = imageUrl.split("/upload/");
            if (parts.length < 2) {
                return null;
            }

            String afterUpload = parts[1];

            // Remove version if present (v1234567890/)
            if (afterUpload.matches("^v\\d+/.*")) {
                afterUpload = afterUpload.substring(afterUpload.indexOf('/') + 1);
            }

            // Remove file extension
            int lastDot = afterUpload.lastIndexOf('.');
            if (lastDot > 0) {
                afterUpload = afterUpload.substring(0, lastDot);
            }

            return afterUpload;
        } catch (Exception e) {
            System.err.println("Failed to extract public ID: " + e.getMessage());
            return null;
        }
    }

    // Get optimized image URL with transformations
    public String getOptimizedImageUrl(String imageUrl, int width, int height) {
        if (imageUrl == null || !imageUrl.contains("cloudinary.com")) {
            return imageUrl;
        }

        try {
            String publicId = extractPublicId(imageUrl);
            if (publicId == null) {
                return imageUrl;
            }

            // Generate optimized URL with transformations
            // Note: For advanced transformations, use Cloudinary's Transformation API
            // For now, return the original URL
            return imageUrl;
        } catch (Exception e) {
            return imageUrl;
        }
    }
}
