/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 * <p>
 * Configuration properties for file storage.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/23/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.configuration;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

/**
 * Configuration properties for file storage.
 * These properties are loaded from application.properties or environment variables.
 */
@ConfigurationProperties(prefix = "file.storage")
@Validated
@Slf4j
@Getter
@Setter
@RequiredArgsConstructor
public class FileStorageProperties {

    @NotBlank(message = "Upload directory must not be blank")
    private final String uploadDir;

    @NotBlank(message = "Public base URL must not be blank")
    @URL(protocol = "https", message = "Public base URL must be a valid HTTPS URL")
    private final String publicBaseUrl;

    @Min(value = 1024, message = "Max file size must be at least 1KB")
    private final long maxFileSize;

    @NotEmpty(message = "Allowed image extensions must not be empty")
    private final List<String> allowedImageExtensions;

    @PostConstruct
    private void validateAndNormalize() {
        // Normalize path
        try {
            Path path = Path.of(uploadDir).normalize().toAbsolutePath();
            log.debug("Normalized uploadDir: {}", path);
        } catch (InvalidPathException e) {
            throw new IllegalArgumentException("Invalid upload directory path: " + uploadDir, e);
        }

        // Normalize extensions (lowercase, must start with dot)
        for (String ext : allowedImageExtensions) {
            if (!ext.startsWith(".")) {
                throw new IllegalArgumentException("Invalid file extension: " + ext + " (must start with dot)");
            }
        }

        // Check for trailing slash in publicBaseUrl
        if (publicBaseUrl.endsWith("/")) {
            throw new IllegalArgumentException("publicBaseUrl must not end with a slash: " + publicBaseUrl);
        }

        log.info("FileStorageProperties initialized: uploadDir={}, publicBaseUrl={}, maxFileSize={}, allowedImageExtensions={}",
                uploadDir, publicBaseUrl, maxFileSize, allowedImageExtensions);
    }

    public List<String> getAllowedImageExtensions() {
        return allowedImageExtensions.stream()
                .map(ext -> ext.toLowerCase(Locale.ROOT))
                .toList();
    }
}