/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * REST controller for managing history content.
 * Provides endpoints for retrieving and updating history information.
 *
 * @author Sebastian Hamm
 * @version 1.1.0
 * @since 7/23/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.controller;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request.HistoryRequest;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ApiResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.HistoryResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.service.HistoryService;
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

/**
 * REST controller for managing history content.
 * Provides endpoints for retrieving and updating history information.
 */
@RestController
@RequestMapping("/history")
@Tag(name = "History", description = "API for managing history content")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;

    /**
     * GET /v1/history : Get history information
     *
     * @return the ResponseEntity with status 200 (OK) and the history information in the body,
     *         or with status 404 (Not Found) if the history information doesn't exist
     */
    @GetMapping
    @Operation(summary = "Get history information", description = "Returns the history information")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved history information",
                    content = @Content(mediaType = "application/json", 
                            schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "History information not found",
                    content = @Content
            )
    })
    public ResponseEntity<ApiResponse<HistoryResponse>> getHistory() {
        ApiResponse<HistoryResponse> response = historyService.getHistory();
        
        if (response.getData() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.<HistoryResponse>builder()
                            .message("History not found")
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .build());
        }
        
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * POST /v1/history : Create history information
     *
     * @param historyRequest the history information to create
     * @return the ResponseEntity with status 201 (Created) and the created history information in the body
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    @Operation(summary = "Create history information", description = "Creates new history information")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "History information created successfully",
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
    public ResponseEntity<ApiResponse<HistoryResponse>> saveHistory(
            @Parameter(description = "History information to create", required = true, schema = @Schema(implementation = HistoryRequest.class))
            @Valid @RequestBody HistoryRequest historyRequest) {
        ApiResponse<HistoryResponse> response = historyService.saveHistory(historyRequest);
        
        if (response.getStatusCode() == HttpStatus.OK.value()) {
            // Change status code to CREATED for successful creation
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.<HistoryResponse>builder()
                            .message(response.getMessage())
                            .statusCode(HttpStatus.CREATED.value())
                            .data(response.getData())
                            .build());
        }
        
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    
    /**
     * PUT /v1/history : Update history information
     *
     * @param historyRequest the history information to update
     * @return the ResponseEntity with status 200 (OK) and the updated history information in the body
     */
    @PutMapping
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    @Operation(summary = "Update history information", description = "Updates existing history information")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "History information updated successfully",
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
                    description = "History information not found",
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
    public ResponseEntity<ApiResponse<HistoryResponse>> updateHistory(
            @Parameter(description = "History information to update", required = true, schema = @Schema(implementation = HistoryRequest.class))
            @Valid @RequestBody HistoryRequest historyRequest) {
        ApiResponse<HistoryResponse> response = historyService.saveHistory(historyRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
