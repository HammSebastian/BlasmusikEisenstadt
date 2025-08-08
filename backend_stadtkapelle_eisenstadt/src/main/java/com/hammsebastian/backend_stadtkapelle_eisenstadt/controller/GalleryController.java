/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * REST controller for managing galleries.
 * Provides endpoints for creating, retrieving, updating, and deleting galleries.
 *
 * @author Sebastian Hamm
 * @version 1.1.0
 * @since 7/23/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.controller;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request.GalleryRequest;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ApiResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.GalleryResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.service.GalleryService;
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

import java.net.URI;
import java.util.List;

/**
 * REST controller for managing galleries.
 * Provides endpoints for creating, retrieving, updating, and deleting galleries.
 */
@RestController
@RequestMapping("/gallery")
@Tag(name = "Gallery", description = "API for managing galleries")
@RequiredArgsConstructor
public class GalleryController {

    private final GalleryService galleryService;

    /**
     * GET /v1/gallery : Get all galleries
     *
     * @return the ResponseEntity with status 200 (OK) and the list of galleries in the body
     */
    @GetMapping
    @Operation(summary = "Get all galleries", description = "Returns a list of all galleries")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved galleries",
                    content = @Content(mediaType = "application/json", 
                            schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content
            )
    })
    public ResponseEntity<ApiResponse<List<GalleryResponse>>> getAllGalleries() {
        ApiResponse<List<GalleryResponse>> response = galleryService.getAllGalleries();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * GET /v1/gallery/:id : Get a gallery by id
     *
     * @param id the id of the gallery to retrieve
     * @return the ResponseEntity with status 200 (OK) and the gallery in the body, or with status 404 (Not Found)
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a gallery by id", description = "Returns a specific gallery by its id")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved gallery",
                    content = @Content(mediaType = "application/json", 
                            schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Gallery not found",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid ID supplied",
                    content = @Content
            )
    })
    public ResponseEntity<ApiResponse<GalleryResponse>> getGallery(
            @Parameter(description = "ID of the gallery to retrieve", required = true)
            @PathVariable Long id) {
        ApiResponse<GalleryResponse> response = galleryService.getGalleryById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * POST /v1/gallery : Create a new gallery
     *
     * @param galleryRequest the gallery to create
     * @return the ResponseEntity with status 201 (Created) and the new gallery in the body
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    @Operation(summary = "Create a new gallery", description = "Creates a new gallery")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Gallery created successfully",
                    content = @Content(mediaType = "application/json", 
                            schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid input",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "Gallery already exists",
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
    public ResponseEntity<ApiResponse<GalleryResponse>> saveGallery(
            @Parameter(description = "Gallery to create", required = true, schema = @Schema(implementation = GalleryRequest.class))
            @Valid @RequestBody GalleryRequest galleryRequest) {
        ApiResponse<GalleryResponse> response = galleryService.createGallery(galleryRequest);
        
        if (response.getStatusCode() == HttpStatus.OK.value()) {
            // Change status code to CREATED for successful creation
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.<GalleryResponse>builder()
                            .message(response.getMessage())
                            .statusCode(HttpStatus.CREATED.value())
                            .data(response.getData())
                            .build());
        }
        
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * PUT /v1/gallery/:id : Update an existing gallery
     *
     * @param id the id of the gallery to update
     * @param galleryRequest the gallery to update
     * @return the ResponseEntity with status 200 (OK) and the updated gallery in the body, or with status 404 (Not Found)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    @Operation(summary = "Update an existing gallery", description = "Updates an existing gallery by id")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Gallery updated successfully",
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
                    description = "Gallery not found",
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
    public ResponseEntity<ApiResponse<GalleryResponse>> updateGallery(
            @Parameter(description = "ID of the gallery to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated gallery data", required = true, schema = @Schema(implementation = GalleryRequest.class))
            @Valid @RequestBody GalleryRequest galleryRequest) {
        ApiResponse<GalleryResponse> response = galleryService.updateGallery(id, galleryRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * DELETE /v1/gallery/:id : Delete a gallery
     *
     * @param id the id of the gallery to delete
     * @return the ResponseEntity with status 204 (No Content) or with status 404 (Not Found)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    @Operation(summary = "Delete a gallery", description = "Deletes a gallery by id")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "204",
                    description = "Gallery deleted successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Gallery not found",
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
    public ResponseEntity<Void> deleteGallery(
            @Parameter(description = "ID of the gallery to delete", required = true)
            @PathVariable Long id) {
        ApiResponse<String> response = galleryService.deleteGallery(id);
        
        if (response.getStatusCode() == HttpStatus.OK.value()) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.status(response.getStatusCode()).build();
    }
}
