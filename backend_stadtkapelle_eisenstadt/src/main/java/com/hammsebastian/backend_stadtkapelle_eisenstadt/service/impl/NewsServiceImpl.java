/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/21/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.service.impl;


import com.hammsebastian.backend_stadtkapelle_eisenstadt.entity.NewEntity;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request.NewsRequest;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ApiResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.NewsResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.repository.NewsRepository;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;

    @Override
    public ApiResponse<NewsResponse> getNew(Long id) {
        Optional<NewEntity> newsEntity = newsRepository.findById(id);
        if (newsEntity.isPresent()) {
            return ApiResponse.<NewsResponse>builder()
                    .message("News retrieved successfully")
                    .statusCode(200)
                    .data(NewsResponse.toNewsResponse(newsEntity.get()))
                    .build();
        } else {
            return ApiResponse.<NewsResponse>builder()
                    .message("News not found")
                    .statusCode(404)
                    .build();
        }
    }

    @Override
    public ApiResponse<NewsResponse> saveNews(NewsRequest newsRequest) {
        Optional<NewEntity> existingNews = newsRepository.findNewsEntityByTitleAndDescription(newsRequest.getTitle(), newsRequest.getDescription());

        if (existingNews.isEmpty()) {
            NewEntity newEntity = NewEntity.builder()
                    .title(newsRequest.getTitle())
                    .description(newsRequest.getDescription())
                    .newsImage(newsRequest.getNewsImage())
                    .newsType(newsRequest.getNewsType())
                    .date(newsRequest.getDate())
                    .isPublished(newsRequest.isPublished())
                    .build();

            NewEntity savedNews = newsRepository.save(newEntity);
            return ApiResponse.<NewsResponse>builder()
                    .message("News saved successfully")
                    .statusCode(201)
                    .data(NewsResponse.toNewsResponse(savedNews))
                    .build();
        } else {
            return ApiResponse.<NewsResponse>builder()
                    .message("News already exists")
                    .statusCode(409)
                    .build();
        }
    }

    @Override
    public ApiResponse<NewsResponse> updateNews(Long id, NewsRequest newsRequest) {
        Optional<NewEntity> existingNews = newsRepository.findById(id);
        if (existingNews.isEmpty()) {
            return ApiResponse.<NewsResponse>builder()
                    .message("News not found")
                    .statusCode(404)
                    .build();
        }

        NewEntity newEntity = existingNews.get();
        newEntity.setTitle(newsRequest.getTitle());
        newEntity.setDescription(newsRequest.getDescription());
        newEntity.setNewsImage(newsRequest.getNewsImage());
        newEntity.setNewsType(newsRequest.getNewsType());
        newEntity.setDate(newsRequest.getDate());
        newEntity.setPublished(newsRequest.isPublished());

        newsRepository.save(newEntity);

        return ApiResponse.<NewsResponse>builder()
                .message("News updated successfully")
                .statusCode(200)
                .data(NewsResponse.toNewsResponse(newEntity))
                .build();
    }

    @Override
    public ApiResponse<String> deleteNews(Long id) {
        if (newsRepository.existsById(id)) {
            newsRepository.deleteById(id);
            return ApiResponse.<String>builder()
                    .message("News deleted")
                    .statusCode(200)
                    .build();
        } else {
            return ApiResponse.<String>builder()
                    .message("News not found")
                    .statusCode(404)
                    .build();
        }
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

    @Override
    public ApiResponse<List<NewsResponse>> getAllNews() {
        List<NewEntity> newsEntities = newsRepository.findAll();
        List<NewsResponse> newsResponses = newsEntities.stream()
                .map(NewsResponse::toNewsResponse)
                .collect(Collectors.toList());

        return ApiResponse.<List<NewsResponse>>builder()
                .message("News retrieved successfully")
                .statusCode(200)
                .data(newsResponses)
                .build();
    }
}
