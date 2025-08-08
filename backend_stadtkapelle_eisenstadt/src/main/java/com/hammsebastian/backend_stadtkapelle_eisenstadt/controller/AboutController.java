/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 * <p>
 * REST controller for managing about page content.
 * Provides endpoints for retrieving, updating, and uploading images for the about page.
 *
 * @author Sebastian Hamm
 * @version 1.1.1
 * @since 7/23/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.controller;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request.AboutRequest;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.AboutResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ApiResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.service.AboutService;
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

/**
 * REST controller for managing about page content.
 * Provides endpoints for retrieving, updating, and uploading images for the about page.
 */
@RestController
@RequestMapping("/about")
@Tag(name = "About", description = "API for managing about page content")
@RequiredArgsConstructor
public class AboutController {

    private final AboutService aboutService;

    /**
     * GET /about: Get about page content
     *
     * @return the ResponseEntity with status 200 (OK) and the about page content in the body,
     *         or with status 404 (Not Found) if the content doesn't exist
     */
    @GetMapping
    @Operation(summary = "Get about page content", description = "Returns the about page content")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved about page content",
                    content = @Content(mediaType = "application/json", 
                            schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "About page content not found",
                    content = @Content
            )
    })
    public ResponseEntity<ApiResponse<AboutResponse>> getAbout() {
        ApiResponse<AboutResponse> response = aboutService.getAbout();

        return response.getData() == null ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResponse.<AboutResponse>builder()
                        .message("About not found")
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .build())
                : ResponseEntity.ok(response);
    }

    /**
     * PUT /about : Update about page content
     *
     * @param aboutRequest the about page content to update
     * @return the ResponseEntity with status 200 (OK) and the updated about page content in the body
     */
    @PutMapping
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    @Operation(summary = "Update about page content", description = "Updates the about page content")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "About page content updated successfully",
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
    public ResponseEntity<ApiResponse<AboutResponse>> updateAbout(
            @Parameter(description = "About page content to update", required = true, schema = @Schema(implementation = AboutRequest.class))
            @Valid @RequestBody AboutRequest aboutRequest) {
        ApiResponse<AboutResponse> response = aboutService.saveAbout(aboutRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * POST /about/images : Upload an image for the about page
     *
     * @param file the image file to upload
     * @return the ResponseEntity with status 201 (Created) and the URL of the uploaded image in the body
     */
    @PostMapping(value = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    @Operation(summary = "Upload an image for the about page", description = "Uploads an image for the about page")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Image uploaded successfully",
                    content = @Content(mediaType = "application/json", 
                            schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid input or file too large",
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
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "415",
                    description = "Unsupported media type",
                    content = @Content
            )
    })
    public ResponseEntity<ApiResponse<String>> uploadImage(
            @Parameter(description = "Image file to upload", required = true)
            @RequestPart("file") MultipartFile file) {
        ApiResponse<String> response = aboutService.uploadImage(file);
        
        if (response.getStatusCode() == HttpStatus.OK.value()) {
            // Change status code to CREATED for successful uploads
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResponse.<String>builder()
                            .message(response.getMessage())
                            .statusCode(HttpStatus.CREATED.value())
                            .data(response.getData())
                            .build()
            );
        }
        
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
