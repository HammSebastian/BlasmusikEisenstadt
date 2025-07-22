/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/21/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.controller;


import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request.NewsRequest;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ApiResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.NewsResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.service.NewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<NewsResponse>>> getAllNews() {
        return ResponseEntity.ok(newsService.getAllNews());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NewsResponse>> getNews(@PathVariable Long id) {
        return ResponseEntity.ok(newsService.getNew(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    public ResponseEntity<ApiResponse<NewsResponse>> saveNews(@Valid @RequestBody NewsRequest newsRequest) {
        return ResponseEntity.ok(newsService.saveNews(newsRequest));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    public ResponseEntity<ApiResponse<NewsResponse>> updateNews(@PathVariable Long id, @Valid @RequestBody NewsRequest newsRequest) {
        return ResponseEntity.ok(newsService.updateNews(id, newsRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    public ResponseEntity<ApiResponse<String>> deleteNews(@PathVariable Long id) {
        ApiResponse<String> response = newsService.deleteNews(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/upload-image")
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    public ResponseEntity<ApiResponse<String>> uploadImage(@RequestParam("file") MultipartFile file) {
        ApiResponse<String> response = newsService.uploadImage(file);
        return ResponseEntity.ok(response);
    }
}
