/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * Service interface for file storage operations.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/23/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.service;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.Optional;

/**
 * Service interface for file storage operations.
 * Provides methods for storing, retrieving, and deleting files.
 */
public interface FileStorageService {

    /**
     * Stores a file in the file system.
     *
     * @param file The file to store
     * @param subdirectory Optional subdirectory within the base upload directory
     * @param filenamePrefix Optional prefix for the generated filename
     * @return The public URL to access the stored file
     * @throws IllegalArgumentException if the file is empty, too large, or has an invalid extension
     * @throws RuntimeException if there was an error storing the file
     */
    String storeFile(MultipartFile file, Optional<String> subdirectory, Optional<String> filenamePrefix);

    /**
     * Stores an image file in the file system, with validation for image file types.
     *
     * @param imageFile The image file to store
     * @param subdirectory Optional subdirectory within the base upload directory
     * @param filenamePrefix Optional prefix for the generated filename
     * @return The public URL to access the stored image
     * @throws IllegalArgumentException if the file is empty, too large, or not a valid image
     * @throws RuntimeException if there was an error storing the file
     */
    String storeImage(MultipartFile imageFile, Optional<String> subdirectory, Optional<String> filenamePrefix);

    /**
     * Deletes a file from the file system.
     *
     * @param fileUrl The public URL of the file to delete
     * @return true if the file was deleted, false otherwise
     */
    boolean deleteFile(String fileUrl);

    /**
     * Gets the Path object for a file in the file system.
     *
     * @param filename The name of the file
     * @param subdirectory Optional subdirectory within the base upload directory
     * @return The Path object for the file
     */
    Path getFilePath(String filename, Optional<String> subdirectory);
}