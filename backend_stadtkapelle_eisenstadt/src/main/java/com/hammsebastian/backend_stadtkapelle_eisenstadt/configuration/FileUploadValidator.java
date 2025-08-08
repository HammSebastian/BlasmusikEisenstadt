/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/25/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.configuration;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.exception.FileValidationException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileUploadValidator {

    private final FileStorageProperties properties;
    private final Tika tika = new Tika();

    private Set<String> allowedExtensions;

    @PostConstruct
    private void init() {
        allowedExtensions = properties.getAllowedImageExtensions().stream()
                .map(String::trim)
                .map(ext -> ext.toLowerCase(Locale.ROOT))
                .collect(Collectors.toUnmodifiableSet());
    }

    public String validateAndGenerateFilename(MultipartFile file) {
        if (file.isEmpty()) {
            log.warn("Rejected empty file upload");
            throw new FileValidationException("Uploaded file is empty.");
        }

        if (file.getSize() > properties.getMaxFileSize()) {
            log.warn("Rejected file exceeding max size: size={} bytes", file.getSize());
            throw new FileValidationException("File size exceeds max allowed limit: " + properties.getMaxFileSize() + " bytes.");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            log.warn("Rejected file with missing filename");
            throw new FileValidationException("Uploaded file must have a valid filename.");
        }

        String extension = "." + FilenameUtils.getExtension(originalFilename).toLowerCase(Locale.ROOT);
        if (!allowedExtensions.contains(extension)) {
            log.warn("Rejected file with disallowed extension: {}", extension);
            throw new FileValidationException("File extension " + extension + " is not allowed. Allowed extensions: " + allowedExtensions);
        }

        if (!isImageMimeType(file)) {
            log.warn("Rejected file with invalid MIME type: {}", file.getContentType());
            throw new FileValidationException("Only image MIME types are allowed.");
        }

        String generatedFilename = UUID.randomUUID() + extension;
        log.debug("Accepted file: original='{}', generated='{}'", originalFilename, generatedFilename);
        return generatedFilename;
    }

    private boolean isImageMimeType(MultipartFile file) {
        try {
            String detectedType = tika.detect(file.getInputStream());
            return detectedType != null && detectedType.toLowerCase(Locale.ROOT).startsWith("image/");
        } catch (IOException e) {
            log.error("Failed to detect MIME type", e);
            throw new FileValidationException("Could not validate file content type.");
        }
    }
}