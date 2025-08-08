/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/22/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.controller;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request.MemberRequest;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ApiResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.LocationResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.MemberResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.repository.MemberRepository;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.service.MemberService;
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

@RestController
@RequestMapping("/members")
@Tag(name = "Member", description = "Endpoints for managing members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;


    /**
     * GET /locations : Get all locations
     *
     * @return the ResponseEntity with status 200 (OK) and the list of locations in the body
     */
    @GetMapping
    @Operation(summary = "Get all members", description = "Returns a list of all members")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved members",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "No members found",
                    content = @Content
            )
    })
    public ResponseEntity<ApiResponse<List<MemberResponse>>> getAllMembers() {
        ApiResponse<List<MemberResponse>> response = memberService.getAllMembers();

        if (response.getData() == null || response.getData().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.<List<MemberResponse>>builder()
                            .message("No members found")
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .build());
        }

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * GET /locations/{id} : Get a location by id
     *
     * @param id the id of the location to retrieve
     * @return the ResponseEntity with status 200 (OK) and the location in the body
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a member by id", description = "Returns a specific member by its id")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved member",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Member not found",
                    content = @Content
            )
    })
    public ResponseEntity<ApiResponse<MemberResponse>> getMember(
            @Parameter(description = "ID of the member to retrieve", required = true)
            @PathVariable Long id) {

        ApiResponse<MemberResponse> response = memberService.getMemberById(id);

        if (response.getData() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.<MemberResponse>builder()
                            .message("Member not found")
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .build());
        }

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * POST /locations : Create a new location
     *
     * @param request the location to create
     * @return the ResponseEntity with status 201 (Created) and the new location in the body
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    @Operation(summary = "Create a new member", description = "Creates a new member")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Member created successfully",
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
    public ResponseEntity<ApiResponse<MemberResponse>> createMember(
            @Parameter(description = "The member to create", required = true, schema = @Schema(implementation = MemberRequest.class))
            @Valid @RequestBody MemberRequest request) {

        ApiResponse<MemberResponse> response = memberService.createMember(request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * PUT /locations/{id} : Update a location
     *
     * @param id    the id of the location to update
     * @param request the location to update
     * @return the ResponseEntity with status 200 (OK) and the updated location in the body
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    @Operation(summary = "Update a member", description = "Updates a member by id")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Member updated successfully",
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
                    description = "Member not found",
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
    public ResponseEntity<ApiResponse<MemberResponse>> updateMember(
            @Parameter(description = "ID of the member to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "The member to update", required = true, schema = @Schema(implementation = MemberRequest.class))
            @Valid @RequestBody MemberRequest request) {

        ApiResponse<MemberResponse> response = memberService.getMemberById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * DELETE /locations/{id} : Delete a location
     *
     * @param id the id of the location to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('write:admin')")
    @Operation(summary = "Delete a member", description = "Deletes a member by id")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Member deleted successfully",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Member not found",
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
    public ResponseEntity<ApiResponse<String>> deleteMember(
            @Parameter(description = "ID of the member to delete", required = true)
            @PathVariable Long id) {

        ApiResponse<String> response = memberService.deleteMember(id);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * GET /locations/count : Get the number of locations
     *
     * @return the ResponseEntity with status 200 (OK) and the number of locations in the body
     */
    @GetMapping("/count")
    @Operation(summary = "Get the number of members", description = "Returns the number of members")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved number of members",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))
            )
    })
    public ResponseEntity<ApiResponse<Long>> countAllMembers() {
        ApiResponse<Long> response = memberService.countAllMembers();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
