/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/28/25
 */
package com.sebastianhamm.Backend.welcome.api.controllers;

import com.sebastianhamm.Backend.welcome.api.dtos.WelcomeRequest;
import com.sebastianhamm.Backend.shared.api.dtos.ApiResponse;
import com.sebastianhamm.Backend.welcome.api.dtos.WelcomeResponse;
import com.sebastianhamm.Backend.welcome.domain.services.WelcomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing the welcome page content.
 * Provides endpoints for retrieving, updating, and uploading images for the welcome page.
 */
@RestController
@RequestMapping("/welcome")
@Tag(name = "Welcome", description = "API for managing welcome page content")

public class WelcomeController {

    private final WelcomeService welcomeService;

    /**
     * GET /welcome: Get welcome page content
     *
     * @return the ResponseEntity with status 200 (OK) and the welcome page content in the body,
     * or with status 404 (Not Found) if the content doesn't exist
     */
    @GetMapping
    @Operation(summary = "Get welcome page content", description = "Returns the welcome page content")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved welcome page content",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Welcome page content not found",
                    content = @Content
            )
    })
    public ResponseEntity<ApiResponse<WelcomeResponse>> getWelcome() {
        ApiResponse<WelcomeResponse> response = welcomeService.getWelcome();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    /**
     * PUT /welcome : Update welcome page content
     *
     * @param welcomeRequest the welcome page content to update
     * @return the ResponseEntity with status 200 (OK) and the updated welcome page content in the body
     */
    @PutMapping
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    @Operation(summary = "Update welcome page content", description = "Updates the welcome page content")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "welcome page content updated successfully",
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
    public ResponseEntity<ApiResponse<WelcomeResponse>> updateWelcome(WelcomeRequest welcomeRequest) {
        ApiResponse<WelcomeResponse> response = welcomeService.updateWelcome(welcomeRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    public WelcomeController(WelcomeService welcomeService) {
        this.welcomeService = welcomeService;
    }
}
