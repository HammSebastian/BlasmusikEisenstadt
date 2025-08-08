/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * REST controller for managing events.
 *
 * @author Sebastian Hamm
 * @version 1.1.0
 * @since 7/23/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.controller;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request.EventRequest;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ApiResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.EventResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.service.EventService;
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

import java.net.URI;
import java.util.List;

/**
 * REST controller for managing events.
 * Provides endpoints for creating, retrieving, updating, and deleting events,
 * as well as uploading event images.
 */
@RestController
@RequestMapping("/events")
@Tag(name = "Events", description = "API for managing events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    /**
     * GET /v1/events : Get all events
     *
     * @return the ResponseEntity with status 200 (OK) and the list of events in the body
     */
    @GetMapping
    @Operation(summary = "Get all events", description = "Returns a list of all events")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved events",
                    content = @Content(mediaType = "application/json", 
                            schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content
            )
    })
    public ResponseEntity<ApiResponse<List<EventResponse>>> getAllEvents() {
        ApiResponse<List<EventResponse>> response = eventService.getAllEvents();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * GET /events/:id : Get an event by id
     *
     * @param id the id of the event to retrieve
     * @return the ResponseEntity with status 200 (OK) and the event in the body, or with status 404 (Not Found)
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get an event by id", description = "Returns a specific event by its id")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved event",
                    content = @Content(mediaType = "application/json", 
                            schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Event not found",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid ID supplied",
                    content = @Content
            )
    })
    public ResponseEntity<ApiResponse<EventResponse>> getEvent(
            @Parameter(description = "ID of the event to retrieve", required = true)
            @PathVariable Long id) {
        ApiResponse<EventResponse> response = eventService.getEvent(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * POST /events : Create a new event
     *
     * @param eventRequest the event to create
     * @return the ResponseEntity with status 201 (Created) and the new event in the body
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    @Operation(summary = "Create a new event", description = "Creates a new event")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Event created successfully",
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
                    description = "Event already exists",
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
    public ResponseEntity<ApiResponse<EventResponse>> createEvent(
            @Parameter(description = "Event to create", required = true, schema = @Schema(implementation = EventRequest.class))
            @Valid @RequestBody EventRequest eventRequest) {
        ApiResponse<EventResponse> response = eventService.saveEvent(eventRequest);
        
        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            return ResponseEntity
                    .created(URI.create("/v1/events/" + response.getData().getId()))
                    .body(response);
        }
        
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * PUT /events/:id : Update an existing event
     *
     * @param id the id of the event to update
     * @param eventRequest the event to update
     * @return the ResponseEntity with status 200 (OK) and the updated event in the body, or with status 404 (Not Found)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    @Operation(summary = "Update an existing event", description = "Updates an existing event by id")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Event updated successfully",
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
                    description = "Event not found",
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
    public ResponseEntity<ApiResponse<EventResponse>> updateEvent(
            @Parameter(description = "ID of the event to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated event data", required = true, schema = @Schema(implementation = EventRequest.class))
            @Valid @RequestBody EventRequest eventRequest) {
        ApiResponse<EventResponse> response = eventService.updateEvent(id, eventRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * DELETE /events/:id : Delete an event
     *
     * @param id the id of the event to delete
     * @return the ResponseEntity with status 204 (No Content) or with status 404 (Not Found)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    @Operation(summary = "Delete an event", description = "Deletes an event by id")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "204",
                    description = "Event deleted successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Event not found",
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
    public ResponseEntity<Void> deleteEvent(
            @Parameter(description = "ID of the event to delete", required = true)
            @PathVariable Long id) {
        ApiResponse<String> response = eventService.deleteEvent(id);
        
        if (response.getStatusCode() == HttpStatus.OK.value()) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.status(response.getStatusCode()).build();
    }

    /**
     * POST /events/images : Upload an event image
     *
     * @param file the image file to upload
     * @return the ResponseEntity with status 201 (Created) and the URL of the uploaded image in the body
     */
    @PostMapping(value = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    @Operation(summary = "Upload an event image", description = "Uploads an image for an event")
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
    public ResponseEntity<ApiResponse<String>> uploadEventImage(
            @Parameter(description = "Image file to upload", required = true)
            @RequestParam("file") MultipartFile file) {
        ApiResponse<String> response = eventService.uploadImage(file);
        
        if (response.getStatusCode() == HttpStatus.OK.value()) {
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
