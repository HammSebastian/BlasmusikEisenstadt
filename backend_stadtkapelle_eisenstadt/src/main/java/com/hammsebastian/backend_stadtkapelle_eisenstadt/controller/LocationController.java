/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * REST controller for managing locations.
 * Provides endpoints for creating, retrieving, updating, and deleting locations,
 * as well as uploading location images.
 *
 * @author Sebastian Hamm
 * @version 1.1.0
 * @since 7/23/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.controller;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request.LocationRequest;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ApiResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.LocationResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.service.LocationService;
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

/**
 * REST controller for managing locations.
 * Provides endpoints for creating, retrieving, updating, and deleting locations,
 * as well as uploading location images.
 */
@RestController
@RequestMapping("/locations")
@Tag(name = "Locations", description = "API for managing locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    /**
     * GET /v1/locations : Get all locations
     *
     * @return the ResponseEntity with status 200 (OK) and the list of locations in the body
     */
    @GetMapping
    @Operation(summary = "Get all locations", description = "Returns a list of all locations")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved locations", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No locations found", content = @Content)})
    public ResponseEntity<ApiResponse<List<LocationResponse>>> getAllLocations() {
        ApiResponse<List<LocationResponse>> response = locationService.getAllLocations();

        if (response.getData() == null || response.getData().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<List<LocationResponse>>builder().message("No locations found").statusCode(HttpStatus.NOT_FOUND.value()).build());
        }

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * GET /v1/locations/{id} : Get a location by id
     *
     * @param id the id of the location to retrieve
     * @return the ResponseEntity with status 200 (OK) and the location in the body, or with status 404 (Not Found)
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a location by id", description = "Returns a specific location by its id")
    @ApiResponses(value = {@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved location", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))), @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Location not found", content = @Content), @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid ID supplied", content = @Content)})
    public ResponseEntity<ApiResponse<LocationResponse>> getLocation(@Parameter(description = "ID of the location to retrieve", required = true) @PathVariable Long id) {
        ApiResponse<LocationResponse> response = locationService.getLocation(id);

        if (response.getData() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<LocationResponse>builder().message("Location not found with id: " + id).statusCode(HttpStatus.NOT_FOUND.value()).build());
        }

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * POST /v1/locations : Create a new location
     *
     * @param locationRequest the location to create
     * @return the ResponseEntity with status 201 (Created) and the new location in the body
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    @Operation(summary = "Create a new location", description = "Creates a new location")
    @ApiResponses(value = {@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Location created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))), @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input", content = @Content), @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content), @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    public ResponseEntity<ApiResponse<LocationResponse>> saveLocation(@Parameter(description = "Location to create", required = true, schema = @Schema(implementation = LocationRequest.class)) @Valid @RequestBody LocationRequest locationRequest) {
        ApiResponse<LocationResponse> response = locationService.saveLocation(locationRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * PUT /v1/locations/{id} : Update an existing location
     *
     * @param id              the id of the location to update
     * @param locationRequest the location to update
     * @return the ResponseEntity with status 200 (OK) and the updated location in the body, or with status 404 (Not Found)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    @Operation(summary = "Update an existing location", description = "Updates an existing location by id")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Location updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Location not found", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<ApiResponse<LocationResponse>> updateLocation(
            @Parameter(description = "ID of the location to update", required = true)
            @PathVariable Long id, @Parameter(description = "Updated location data", required = true, schema = @Schema(implementation = LocationRequest.class))
            @Valid @RequestBody LocationRequest locationRequest) {
        ApiResponse<LocationResponse> response = locationService.updateLocation(id, locationRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * DELETE /v1/locations/{id} : Delete a location
     *
     * @param id the id of the location to delete
     * @return the ResponseEntity with status 204 (No Content) or with status 404 (Not Found)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    @Operation(summary = "Delete a location", description = "Deletes a location by id")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Location deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Location not found", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<Void> deleteLocation(
            @Parameter(description = "ID of the location to delete", required = true)
            @PathVariable Long id) {
        ApiResponse<String> response = locationService.deleteLocation(id);

        return ResponseEntity.status(response.getStatusCode()).build();
    }

    /**
     * POST /v1/locations/images : Upload an image for a location
     *
     * @param file the image file to upload
     * @return the ResponseEntity with status 201 (Created) and the URL of the uploaded image in the body
     */
    @PostMapping(value = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    @Operation(summary = "Upload an image for a location", description = "Uploads an image for a location")
    @ApiResponses(value = {@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Image uploaded successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))), @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input or file too large", content = @Content), @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content), @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden", content = @Content), @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "415", description = "Unsupported media type", content = @Content)})
    public ResponseEntity<ApiResponse<String>> uploadImage(@Parameter(description = "Image file to upload", required = true) @RequestParam("file") MultipartFile file) {
        ApiResponse<String> response = locationService.uploadImage(file);

        if (response.getStatusCode() == HttpStatus.OK.value()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<String>builder().message(response.getMessage()).statusCode(HttpStatus.CREATED.value()).data(response.getData()).build());
        }

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
