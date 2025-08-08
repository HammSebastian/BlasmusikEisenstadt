/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * REST controller for managing images.
 * Provides endpoints for creating, retrieving, and deleting images.
 *
 * @author Sebastian Hamm
 * @version 1.1.0
 * @since 7/23/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.controller;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request.ImageRequest;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ApiResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ImageResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing images.
 * Provides endpoints for creating, retrieving, and deleting images.
 */
@RestController
@RequestMapping("/images")
@Tag(name = "Images", description = "API for managing images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    /**
     * GET /v1/images : Get all images
     *
     * @return the ResponseEntity with status 200 (OK) and the list of images in the body
     */
    @GetMapping
    @Operation(summary = "Get all images", description = "Returns a list of all images")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved images",
                    content = @Content(mediaType = "application/json", 
                            schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content
            )
    })
    public ResponseEntity<ApiResponse<List<ImageResponse>>> getAllImages() {
        // This is a new endpoint that would need to be implemented in the service
        // For now, we'll return a not implemented response
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                ApiResponse.<List<ImageResponse>>builder()
                        .message("Get all images not implemented yet")
                        .statusCode(HttpStatus.NOT_IMPLEMENTED.value())
                        .build());
    }

    /**
     * GET /v1/images/{title} : Get images by gallery title
     *
     * @param title the title of the gallery to retrieve images for
     * @return the ResponseEntity with status 200 (OK) and the list of images in the body, or with status 404 (Not Found)
     */
    @GetMapping("/{title}")
    @Operation(summary = "Get images by gallery title", description = "Returns a list of images for a specific gallery")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved images",
                    content = @Content(mediaType = "application/json", 
                            schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Gallery not found or has no images",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid title supplied",
                    content = @Content
            )
    })
    public ResponseEntity<ApiResponse<List<ImageResponse>>> getImagesByTitle(
            @Parameter(description = "Title of the gallery to retrieve images for", required = true)
            @PathVariable String title) {
        ApiResponse<List<ImageResponse>> response = imageService.getImagesByGalleryTitle(title);
        
        if (response.getData() == null || response.getData().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.<List<ImageResponse>>builder()
                            .message("No images found for gallery: " + title)
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .build());
        }
        
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * POST /v1/images : Create a new image
     *
     * @param imageRequest the image to create
     * @return the ResponseEntity with status 201 (Created) and the new image in the body
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    @Operation(summary = "Create a new image", description = "Creates a new image")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Image created successfully",
                    content = @Content(mediaType = "application/json", 
                            schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid input",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content
            )
    })
    public ResponseEntity<ApiResponse<ImageResponse>> saveImage(
            @Parameter(description = "Image to create", required = true, schema = @Schema(implementation = ImageRequest.class))
            @Valid @RequestBody ImageRequest imageRequest) {
        ApiResponse<ImageResponse> response = imageService.saveImage(imageRequest);
        
        if (response.getStatusCode() == HttpStatus.OK.value()) {
            // Change status code to CREATED for successful creation
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.<ImageResponse>builder()
                            .message(response.getMessage())
                            .statusCode(HttpStatus.CREATED.value())
                            .data(response.getData())
                            .build());
        }
        
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    
    /**
     * PUT /v1/images/{id} : Update an existing image
     *
     * @param id the id of the image to update
     * @param imageRequest the image to update
     * @return the ResponseEntity with status 200 (OK) and the updated image in the body, or with status 404 (Not Found)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    @Operation(summary = "Update an existing image", description = "Updates an existing image by id")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Image updated successfully",
                    content = @Content(mediaType = "application/json", 
                            schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid input",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Image not found",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content
            )
    })
    public ResponseEntity<ApiResponse<ImageResponse>> updateImage(
            @Parameter(description = "ID of the image to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated image data", required = true, schema = @Schema(implementation = ImageRequest.class))
            @Valid @RequestBody ImageRequest imageRequest) {
        // This is a new endpoint that would need to be implemented in the service
        // For now, we'll return a not implemented response
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                ApiResponse.<ImageResponse>builder()
                        .message("Update image not implemented yet")
                        .statusCode(HttpStatus.NOT_IMPLEMENTED.value())
                        .build());
    }

    /**
     * DELETE /v1/images/{id} : Delete an image
     *
     * @param id the id of the image to delete
     * @return the ResponseEntity with status 204 (No Content) or with status 404 (Not Found)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    @Operation(summary = "Delete an image", description = "Deletes an image by id")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "204",
                    description = "Image deleted successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Image not found",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content
            )
    })
    public ResponseEntity<Void> deleteImage(
            @Parameter(description = "ID of the image to delete", required = true)
            @PathVariable Long id) {
        ApiResponse<String> response = imageService.deleteImage(id);
        
        if (response.getStatusCode() == HttpStatus.OK.value()) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.status(response.getStatusCode()).build();
    }

}
