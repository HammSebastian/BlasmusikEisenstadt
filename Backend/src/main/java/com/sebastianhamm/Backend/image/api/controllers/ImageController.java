/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/28/25
 */
package com.sebastianhamm.Backend.image.api.controllers;
import com.sebastianhamm.Backend.gallery.domain.entities.GalleryEntity;

import com.sebastianhamm.Backend.shared.api.dtos.ApiResponse;
import com.sebastianhamm.Backend.image.api.dtos.ImageResponse;
import com.sebastianhamm.Backend.image.domain.services.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/images")
@Tag(name = "Image Management", description = "Operations related to images of the Stadtkapelle Eisenstadt")
public class ImageController {

    private final ImageService imageService;

    /**
     * POST /images/upload : Upload a new image
     */
    @PostMapping("/upload")
    @PreAuthorize("hasAnyAuthority('write:admin')")
    @Operation(summary = "Upload a new image", description = "Uploads a new image with associated context")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Image uploaded successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid file or context", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<ApiResponse<ImageResponse>> uploadImage(@RequestParam("file") MultipartFile file,
                                                                  @RequestParam("context") String context) {
        ApiResponse<ImageResponse> response = imageService.saveImage(file, context);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * PUT /images/{id} : Update an existing image
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('write:admin')")
    @Operation(summary = "Update an existing image", description = "Updates an image by id and new data")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Image updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Image not found", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<ApiResponse<ImageResponse>> updateImage(@PathVariable Long id,
                                                                  @RequestParam("file") MultipartFile file,
                                                                  @RequestParam("context") String context) {
        ApiResponse<ImageResponse> response = imageService.updateImage(id, file, context);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * GET /images/{id} : Get an image by id
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get image by id", description = "Retrieves an image by its ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved image", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Image not found", content = @Content)
    })
    public ResponseEntity<ApiResponse<ImageResponse>> getImageById(@PathVariable Long id) {
        ApiResponse<ImageResponse> response = imageService.findById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * GET /images : Get all images
     */
    @GetMapping
    @Operation(summary = "Get all images", description = "Returns a list of all uploaded images")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved image list", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No images found", content = @Content)
    })
    public ResponseEntity<ApiResponse<List<ImageResponse>>> getAllImages() {
        ApiResponse<List<ImageResponse>> response = imageService.findAll();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * GET /images/slug/{slug} : Get images by slug
     */
    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get images by slug", description = "Returns a list of images by associated context/slug")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved images", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No images found for slug", content = @Content)
    })
    public ResponseEntity<ApiResponse<List<ImageResponse>>> getImagesBySlug(@PathVariable String slug) {
        ApiResponse<List<ImageResponse>> response = imageService.findBySlug(slug);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * DELETE /images/{id} : Delete an image by id
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('write:admin')")
    @Operation(summary = "Delete an image", description = "Deletes an image by its ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Image deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Image not found", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<ApiResponse<String>> deleteImage(@PathVariable Long id) {
        ApiResponse<String> response = imageService.delete(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }
}
