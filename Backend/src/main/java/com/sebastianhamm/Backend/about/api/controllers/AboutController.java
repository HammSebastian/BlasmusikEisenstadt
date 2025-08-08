/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/28/25
 */
package com.sebastianhamm.Backend.about.api.controllers;

import com.sebastianhamm.Backend.about.api.dtos.AboutRequest;
import com.sebastianhamm.Backend.about.api.dtos.AboutResponse;
import com.sebastianhamm.Backend.shared.api.dtos.ApiResponse;
import com.sebastianhamm.Backend.about.domain.services.AboutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * REST controller for managing about page content.
 * Provides endpoints for retrieving, updating, and uploading images for the about page.
 */
@RestController
@RequestMapping("/about")
@Tag(name = "About", description = "API for managing about page content")
public class AboutController {

    private final AboutService aboutService;

    /**
     * GET /about: Get about page content
     *
     * @return the ResponseEntity with status 200 (OK) and the about page content in the body,
     * or with status 404 (Not Found) if the content doesn't exist
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
        return ResponseEntity.status(response.getStatusCode()).body(response);
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
        ApiResponse<AboutResponse> response = aboutService.updateAbout(aboutRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    public AboutController(AboutService aboutService) {
        this.aboutService = aboutService;
    }
}
