/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * REST controller for managing news.
 * Provides endpoints for creating, retrieving, updating, and deleting news,
 * as well as uploading news images.
 *
 * @author Sebastian Hamm
 * @version 1.1.0
 * @since 7/25/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.controller;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request.NewsRequest;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ApiResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.NewsResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/news")
@Tag(name = "News", description = "API for managing news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping
    @Operation(summary = "Get all news", description = "Returns a list of all news")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved news", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No news found", content = @Content)
    })
    public ResponseEntity<ApiResponse<List<NewsResponse>>> getAllNews() {
        ApiResponse<List<NewsResponse>> response = newsService.getAllNews();
        if (response.getData() == null || response.getData().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<List<NewsResponse>>builder()
                            .message("No news found")
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .build());
        }
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a news entry by id", description = "Returns a specific news entry by its id")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved news", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "News not found", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid ID supplied", content = @Content)
    })
    public ResponseEntity<ApiResponse<NewsResponse>> getNews(
            @Parameter(description = "ID of the news to retrieve", required = true)
            @PathVariable Long id) {
        ApiResponse<NewsResponse> response = newsService.getNew(id);
        if (response.getData() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<NewsResponse>builder()
                            .message("News not found with id: " + id)
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .build());
        }
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    @Operation(summary = "Create a news entry", description = "Creates a new news entry")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "News created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<ApiResponse<NewsResponse>> saveNews(
            @Parameter(description = "News to create", required = true, schema = @Schema(implementation = NewsRequest.class))
            @Valid @RequestBody NewsRequest newsRequest) {
        ApiResponse<NewsResponse> response = newsService.saveNews(newsRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    @Operation(summary = "Update a news entry", description = "Updates an existing news entry by id")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "News updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "News not found", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<ApiResponse<NewsResponse>> updateNews(
            @Parameter(description = "ID of the news to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated news data", required = true, schema = @Schema(implementation = NewsRequest.class))
            @Valid @RequestBody NewsRequest newsRequest) {
        ApiResponse<NewsResponse> response = newsService.updateNews(id, newsRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    @Operation(summary = "Delete a news entry", description = "Deletes a news entry by id")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "News deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "News not found", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<Void> deleteNews(
            @Parameter(description = "ID of the news to delete", required = true)
            @PathVariable Long id) {
        ApiResponse<String> response = newsService.deleteNews(id);
        return ResponseEntity.status(response.getStatusCode()).build();
    }

    @PostMapping(value = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    @Operation(summary = "Upload an image for news", description = "Uploads an image for a news entry")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Image uploaded successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input or file too large", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "415", description = "Unsupported media type", content = @Content)
    })
    public ResponseEntity<ApiResponse<String>> uploadImage(
            @Parameter(description = "Image file to upload", required = true)
            @RequestParam("file") MultipartFile file) {
        ApiResponse<String> response = newsService.uploadImage(file);
        if (response.getStatusCode() == HttpStatus.OK.value()) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.<String>builder()
                            .message(response.getMessage())
                            .statusCode(HttpStatus.CREATED.value())
                            .data(response.getData())
                            .build());
        }
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
