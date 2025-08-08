/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 * <p>
 * Implementation of the AboutService.
 * Handles business logic related to the "About" page content and image uploads.
 * All responses are wrapped in ApiResponse objects for frontend compatibility.
 *
 * @author Sebastian Hamm
 * @version 1.1.1
 * @since 7/23/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.service.impl;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.configuration.FileStorageProperties;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.entity.AboutEntity;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request.AboutRequest;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.AboutResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ApiResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.repository.AboutRepository;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.service.AboutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AboutServiceImpl implements AboutService {

    private static final Long FIXED_ABOUT_ID = 1L;
    private static final String IMAGE_URL_PREFIX = "/api/v1/images/";
    private static final long MAX_FILE_SIZE_BYTES = 5_000_000L; // 5 MB max file size
    private static final List<String> ALLOWED_EXTENSIONS = List.of(".jpg", ".jpeg", ".png", ".webp");

    private final AboutRepository aboutRepository;
    private final FileStorageProperties fileStorageProperties;

    private Path getUploadDir() {
        return Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath()
                .normalize();
    }

    @Override
    public ApiResponse<AboutResponse> getAbout() {
        return aboutRepository.findById(FIXED_ABOUT_ID)
                .map(entity -> ApiResponse.<AboutResponse>builder()
                        .message("About page content retrieved successfully")
                        .statusCode(HttpStatus.OK.value())
                        .data(AboutResponse.builder()
                                .aboutText(entity.getAboutText())
                                .aboutImage(entity.getAboutImageUrl())
                                .build())
                        .errors(null)
                        .build())
                .orElse(ApiResponse.<AboutResponse>builder()
                        .message("About page content not found")
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .errors(new String[] {"No about content found for id " + FIXED_ABOUT_ID})
                        .build());
    }

    @Override
    public ApiResponse<AboutResponse> saveAbout(AboutRequest aboutRequest) {
        AboutEntity entity = aboutRepository.findById(FIXED_ABOUT_ID)
                .orElseGet(() -> {
                    AboutEntity newEntity = new AboutEntity();
                    newEntity.setId(FIXED_ABOUT_ID);
                    return newEntity;
                });

        String aboutText = aboutRequest.getAboutText() != null ? aboutRequest.getAboutText().trim() : null;
        String aboutImageUrl = aboutRequest.getAboutImageUrl() != null ? aboutRequest.getAboutImageUrl().trim() : null;

        entity.setAboutText(aboutText);
        entity.setAboutImageUrl(aboutImageUrl);
        aboutRepository.save(entity);

        log.info("About page content updated");

        return ApiResponse.<AboutResponse>builder()
                .message("About page content saved successfully")
                .statusCode(HttpStatus.OK.value())
                .data(AboutResponse.builder()
                        .aboutText(entity.getAboutText())
                        .aboutImage(entity.getAboutImageUrl())
                        .build())
                .errors(null)
                .build();
    }

    @Override
    public ApiResponse<String> uploadImage(MultipartFile file) {
        if (file.isEmpty()) {
            return ApiResponse.<String>builder()
                    .message("File is empty")
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .errors(new String[] {"Uploaded file is empty"})
                    .build();
        }

        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            return ApiResponse.<String>builder()
                    .message("File size exceeds limit of 5 MB")
                    .statusCode(HttpStatus.PAYLOAD_TOO_LARGE.value())
                    .errors(new String[] {"File size: " + file.getSize() + " bytes exceeds max allowed: " + MAX_FILE_SIZE_BYTES})
                    .build();
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ApiResponse.<String>builder()
                    .message("Invalid file type; only image files are allowed")
                    .statusCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                    .errors(new String[] {"Content type: " + contentType})
                    .build();
        }

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        if (originalFilename.contains("..")) {
            return ApiResponse.<String>builder()
                    .message("Invalid filename detected")
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .errors(new String[] {"Filename contains invalid path sequence: " + originalFilename})
                    .build();
        }

        String ext = originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf('.')).toLowerCase()
                : "";

        if (!ALLOWED_EXTENSIONS.contains(ext)) {
            return ApiResponse.<String>builder()
                    .message("Unsupported file extension; allowed: .jpg, .jpeg, .png, .webp")
                    .statusCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                    .errors(new String[] {"File extension: " + ext})
                    .build();
        }

        try {
            Path uploadDir = getUploadDir();
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
                log.info("Created upload directory: {}", uploadDir);
            }

            String filename = "site-bg-" + UUID.randomUUID() + ext;
            Path targetLocation = uploadDir.resolve(filename);

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            String publicUrl = IMAGE_URL_PREFIX + filename;

            log.info("Image uploaded successfully: {}", filename);

            return ApiResponse.<String>builder()
                    .message("Image uploaded successfully")
                    .statusCode(HttpStatus.CREATED.value())
                    .data(publicUrl)
                    .errors(null)
                    .build();
        } catch (IOException e) {
            log.error("Failed to save uploaded image: {}", e.getMessage(), e);
            return ApiResponse.<String>builder()
                    .message("Internal server error while saving file")
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .errors(new String[] {e.getMessage()})
                    .build();
        }
    }
}