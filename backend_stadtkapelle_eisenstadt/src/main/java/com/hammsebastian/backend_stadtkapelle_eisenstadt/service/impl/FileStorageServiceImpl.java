/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * Implementation of the FileStorageService interface.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/23/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.service.impl;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.configuration.FileStorageProperties;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.service.FileStorageService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the FileStorageService interface.
 * Handles file storage operations using the file system.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

    private final FileStorageProperties fileStorageProperties;
    private Path fileStorageLocation;

    /**
     * Initializes the file storage location.
     * Creates the directory if it doesn't exist.
     */
    @PostConstruct
    public void init() {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
            log.info("Created file storage directory: {}", this.fileStorageLocation);
        } catch (IOException ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored", ex);
        }
    }

    @Override
    public String storeFile(MultipartFile file, Optional<String> subdirectory, Optional<String> filenamePrefix) {
        validateFile(file);

        // Create subdirectory if provided
        Path targetLocation = subdirectory
                .map(sub -> this.fileStorageLocation.resolve(sub))
                .orElse(this.fileStorageLocation);

        try {
            if (!Files.exists(targetLocation)) {
                Files.createDirectories(targetLocation);
                log.info("Created subdirectory: {}", targetLocation);
            }

            // Generate a unique filename
            String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            String fileExtension = getFileExtension(originalFilename);
            String newFilename = filenamePrefix.orElse("file-") + UUID.randomUUID() + fileExtension;

            // Copy the file to the target location
            Path targetPath = targetLocation.resolve(newFilename);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            log.info("Stored file: {}", targetPath);

            // Generate the public URL
            String publicUrl = generatePublicUrl(subdirectory.orElse(""), newFilename);
            log.info("Public URL: {}", publicUrl);

            return publicUrl;
        } catch (IOException ex) {
            log.error("Failed to store file", ex);
            throw new RuntimeException("Failed to store file", ex);
        }
    }

    @Override
    public String storeImage(MultipartFile imageFile, Optional<String> subdirectory, Optional<String> filenamePrefix) {
        validateImageFile(imageFile);
        return storeFile(imageFile, subdirectory, filenamePrefix.or(() -> Optional.of("image-")));
    }

    @Override
    public boolean deleteFile(String fileUrl) {
        try {
            String baseUrl = fileStorageProperties.getPublicBaseUrl();
            if (baseUrl == null || !fileUrl.startsWith(baseUrl)) {
                log.warn("File URL does not start with base URL, cannot resolve file path: {}", fileUrl);
                return false;
            }

            String relativePath = fileUrl.substring(baseUrl.length());
            if (relativePath.startsWith("/")) {
                relativePath = relativePath.substring(1);
            }

            Path filePath = fileStorageLocation.resolve(relativePath).normalize();
            if (!filePath.startsWith(fileStorageLocation)) {
                // Sicherheitscheck: Kein Zugriff außerhalb des Verzeichnisses!
                log.warn("File path outside storage location attempted: {}", filePath);
                return false;
            }

            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.error("Failed to delete file: {}", fileUrl, e);
            return false;
        }
    }


    @Override
    public Path getFilePath(String filename, Optional<String> subdirectory) {
        Path targetLocation = subdirectory
                .map(sub -> this.fileStorageLocation.resolve(sub))
                .orElse(this.fileStorageLocation);
        return targetLocation.resolve(filename);
    }

    /**
     * Validates that the file is not empty and not too large.
     *
     * @param file The file to validate
     * @throws IllegalArgumentException if the file is empty or too large
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        if (file.getSize() > fileStorageProperties.getMaxFileSize()) {
            throw new IllegalArgumentException("File size exceeds the maximum allowed size of " 
                    + (fileStorageProperties.getMaxFileSize() / (1024 * 1024)) + "MB");
        }
    }

    /**
     * Validates that the file is a valid image file.
     *
     * @param file The file to validate
     * @throws IllegalArgumentException if the file is not a valid image
     */
    private void validateImageFile(MultipartFile file) {
        validateFile(file);

        String extension = getFileExtension(Objects.requireNonNull(file.getOriginalFilename()));
        boolean allowed = fileStorageProperties.getAllowedImageExtensions().stream()
                .anyMatch(ext -> ext.equalsIgnoreCase(extension));
        if (!allowed) {
            throw new IllegalArgumentException("Only image files are allowed. Supported: "
                    + String.join(", ", fileStorageProperties.getAllowedImageExtensions()));
        }
    }

    /**
     * Gets the file extension from a filename.
     *
     * @param filename The filename
     * @return The file extension (including the dot)
     */
    private String getFileExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        return lastDot > 0 ? filename.substring(lastDot + 1).toLowerCase() : "";
    }

    /**
     * Generates a public URL for accessing the file.
     *
     * @param subdirectory The subdirectory within the base upload directory
     * @param filename The filename
     * @return The public URL
     */
    private String generatePublicUrl(String subdirectory, String filename) {
        String baseUrl = fileStorageProperties.getPublicBaseUrl();
        if (baseUrl == null || baseUrl.isEmpty()) {
            // Fallback to a default URL if not configured
            baseUrl = "http://localhost:8081/api/v1/files/";
            log.warn("Public base URL not configured. Using default: {}", baseUrl);
        }
        
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }
        
        if (!subdirectory.isEmpty()) {
            if (subdirectory.startsWith("/")) {
                subdirectory = subdirectory.substring(1);
            }
            if (!subdirectory.endsWith("/")) {
                subdirectory += "/";
            }
            return baseUrl + subdirectory + filename;
        } else {
            return baseUrl + filename;
        }
    }
}