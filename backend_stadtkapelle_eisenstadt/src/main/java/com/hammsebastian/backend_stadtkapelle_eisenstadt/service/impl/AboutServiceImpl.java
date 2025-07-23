package com.hammsebastian.backend_stadtkapelle_eisenstadt.service.impl;

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

    private final AboutRepository aboutRepository;

    private static final Long FIXED_ABOUT_ID = 1L;
    private static final String IMAGE_URL_PREFIX = "/api/v1/images/";
    private static final long MAX_FILE_SIZE_BYTES = 5_000_000L;
    private static final List<String> ALLOWED_EXTENSIONS = List.of(".jpg", ".jpeg", ".png", ".webp");

    @Value("${app.upload.dir}")
    private String uploadDirPath;

    private Path getUploadDir() {
        return Paths.get(uploadDirPath);
    }

    @Override
    public ApiResponse<AboutResponse> getAbout() {
        return aboutRepository.findById(FIXED_ABOUT_ID)
                .map(entity -> ApiResponse.<AboutResponse>builder()
                        .message("Site content found")
                        .statusCode(HttpStatus.OK.value())
                        .data(AboutResponse.builder()
                                .aboutText(entity.getAboutText())
                                .aboutImage(entity.getAboutImage())
                                .build())
                        .build())
                .orElse(ApiResponse.<AboutResponse>builder()
                        .message("About not found")
                        .statusCode(HttpStatus.NOT_FOUND.value())
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

        entity.setAboutText(aboutRequest.getAboutText());
        entity.setAboutImage(aboutRequest.getAboutImage());
        aboutRepository.save(entity);

        return ApiResponse.<AboutResponse>builder()
                .message("Site content saved")
                .statusCode(HttpStatus.OK.value())
                .data(AboutResponse.builder()
                        .aboutText(entity.getAboutText())
                        .aboutImage(entity.getAboutImage())
                        .build())
                .build();
    }

    @Override
    public ApiResponse<String> uploadImage(MultipartFile file) {
        if (file.isEmpty()) {
            return ApiResponse.<String>builder()
                    .message("File is empty")
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .build();
        }

        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            return ApiResponse.<String>builder()
                    .message("File is too large. Max size is 5MB.")
                    .statusCode(HttpStatus.PAYLOAD_TOO_LARGE.value())
                    .build();
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ApiResponse.<String>builder()
                    .message("Invalid file type. Only images are allowed.")
                    .statusCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                    .build();
        }

        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String ext = originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf('.')).toLowerCase()
                : "";

        if (!ALLOWED_EXTENSIONS.contains(ext)) {
            return ApiResponse.<String>builder()
                    .message("Invalid file extension. Allowed: .jpg, .jpeg, .png, .webp")
                    .statusCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                    .build();
        }

        try {
            Path uploadDir = getUploadDir();
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            String filename = "site-bg-" + UUID.randomUUID() + ext;
            Path targetPath = uploadDir.resolve(filename);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            return ApiResponse.<String>builder()
                    .message("Upload erfolgreich")
                    .statusCode(HttpStatus.OK.value())
                    .data(IMAGE_URL_PREFIX + filename)
                    .build();
        } catch (IOException e) {
            log.error("Fehler beim Upload der Datei: {}", e.getMessage(), e);
            return ApiResponse.<String>builder()
                    .message("Fehler beim Speichern der Datei")
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .build();
        }
    }
}
