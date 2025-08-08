/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/28/25
 */
package com.sebastianhamm.Backend.member.api.controllers;

import com.sebastianhamm.Backend.member.api.dtos.MemberRequest;
import com.sebastianhamm.Backend.shared.api.dtos.ApiResponse;
import com.sebastianhamm.Backend.member.api.dtos.MemberResponse;
import com.sebastianhamm.Backend.member.domain.services.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
@Tag(name = "Member Management", description = "Operations related to members of the Stadtkapelle Eisenstadt")

public class MemberController {

    private final MemberService memberService;


    /**
     * POST /v1/members : Create a new member
     *
     * @param memberRequest the member to create
     * @return the ResponseEntity with status 201 (Created) and the new member in the body
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    @Operation(summary = "Create a new member", description = "Creates a new member")
    @ApiResponses(value = {@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "member created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))), @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input", content = @Content), @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content), @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    public ResponseEntity<ApiResponse<MemberResponse>> createMember(@Valid @RequestBody MemberRequest memberRequest) {
        ApiResponse<MemberResponse> response = memberService.create(memberRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * GET /v1/members/{id} : Get a member by id
     *
     * @param id the id of the member to retrieve
     * @return the ResponseEntity with status 200 (OK) and the member in the body, or with status 404 (Not Found)
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a member by id", description = "Returns a specific member by its id")
    @ApiResponses(value = {@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved member", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))), @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "member not found", content = @Content), @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid ID supplied", content = @Content)})
    public ResponseEntity<ApiResponse<MemberResponse>> getMemberById(@PathVariable Long id) {
        ApiResponse<MemberResponse> response = memberService.findById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    /**
     * GET /v1/members : Get all members
     *
     * @return the ResponseEntity with status 200 (OK) and the list of members in the body
     */
    @GetMapping
    @Operation(summary = "Get all members", description = "Returns a list of all members")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved members", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No members found", content = @Content)})
    public ResponseEntity<ApiResponse<List<MemberResponse>>> getAllMembers() {
        ApiResponse<List<MemberResponse>> response = memberService.findAll();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * PUT /v1/members/{id} : Update an existing member
     *
     * @param id            the id of the member to update
     * @param memberRequest the member to update
     * @return the ResponseEntity with status 200 (OK) and the updated member in the body, or with status 404 (Not Found)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    @Operation(summary = "Update an existing member", description = "Updates an existing member by id")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "member updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "member not found", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<ApiResponse<MemberResponse>> updateMember(@PathVariable Long id,
                                                                    @Valid @RequestBody MemberRequest memberRequest) {
        ApiResponse<MemberResponse> response = memberService.update(id, memberRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * DELETE /v1/member/{id} : Delete a member
     *
     * @param id the id of the member to delete
     * @return the ResponseEntity with status 204 (No Content) or with status 404 (Not Found)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    @Operation(summary = "Delete a member", description = "Deletes a member by id")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "member deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "member not found", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<ApiResponse<String>> deleteMember(@PathVariable Long id) {
        ApiResponse<String> response = memberService.delete(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
}
