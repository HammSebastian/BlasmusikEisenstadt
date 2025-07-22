/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/20/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.service.impl;


import com.hammsebastian.backend_stadtkapelle_eisenstadt.entity.AboutEntity;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.entity.WelcomeContentEntity;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request.AboutRequest;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.AboutResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ApiResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.WelcomeContentResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.repository.AboutRepository;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.service.AboutService;
import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class AboutServiceImpl implements AboutService {

    private final AboutRepository aboutRepository;

    @Override
    public ApiResponse<AboutResponse> getAbout() {

        Optional<AboutEntity> aboutEntityOptional = aboutRepository.findById(1L);

        if (aboutEntityOptional.isEmpty()) {
            return ApiResponse.<AboutResponse>builder()
                    .message("About not found")
                    .statusCode(404)
                    .build();
        }

        AboutEntity aboutEntity = aboutEntityOptional.get();
        AboutResponse aboutResponse = AboutResponse.builder()
                .aboutText(aboutEntity.getAboutText())
                .aboutImage(aboutEntity.getAboutImage())
                .build();
        return ApiResponse.<AboutResponse>builder()
                .message("SiteContent found")
                .statusCode(200)
                .data(aboutResponse)
                .build();
    }

    @Override
    public ApiResponse<AboutResponse> saveAbout(AboutRequest aboutRequest) {

        Optional<AboutEntity> existingContentOptional = aboutRepository.findById(1L);
        AboutEntity aboutEntity;

        if (existingContentOptional.isPresent()) {
            aboutEntity = existingContentOptional.get();
        } else {
            aboutEntity = new AboutEntity();
            aboutEntity.setId(1L);
        }

        aboutEntity.setAboutText(aboutRequest.getAboutText());
        aboutEntity.setAboutImage(aboutRequest.getAboutImage());

        aboutRepository.save(aboutEntity);

        return ApiResponse.<AboutResponse>builder()
                .message("SiteContent saved/updated successfully")
                .statusCode(200)
                .data(AboutResponse.builder()
                        .aboutText(aboutEntity.getAboutText())
                        .aboutImage(aboutEntity.getAboutImage())
                        .build())
                .build();
    }

    @Override
    public ApiResponse<String> uploadImage(MultipartFile file) {
        final Path uploadDirectory = Paths.get("src/main/resources/static/images");

        if (!Files.exists(uploadDirectory)) {
            try {
                Files.createDirectories(uploadDirectory);
            } catch (IOException e) {
                return ApiResponse.<String>builder()
                        .message("Could not create upload directory")
                        .statusCode(500)
                        .data(null)
                        .build();
            }
        }

        if (file.isEmpty()) {
            return ApiResponse.<String>builder()
                    .message("Please select a file")
                    .statusCode(400)
                    .data(null)
                    .build();
        }

        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String ext = "";

        int extIndex = filename.lastIndexOf('.');
        if (extIndex > 0) {
            ext = filename.substring(extIndex);
        }
        String newFilename = "site-bg-" + System.currentTimeMillis() + ext;

        try {
            Path targetPath = uploadDirectory.resolve(newFilename);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            String publicPath = "http://localhost:8081/api/v1/images/" + newFilename; // TODO: Production Pfad anpassen

            return ApiResponse.<String>builder()
                    .message("Upload erfolgreich")
                    .statusCode(200)
                    .data(publicPath)
                    .build();
        } catch (IOException e) {
            return ApiResponse.<String>builder()
                    .message("Fehler beim Upload")
                    .statusCode(500)
                    .build();
        }
    }
}
