/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/22/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.controller;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request.SectionRequest;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ApiResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.SectionResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.service.SectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sections")
@RequiredArgsConstructor
public class SectionController {

    private final SectionService sectionService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SectionResponse>>> getAllSections() {
        return ResponseEntity.ok(sectionService.getAllSections());
    }

    @GetMapping("/year/{year}")
    public ResponseEntity<ApiResponse<List<SectionResponse>>> getSectionsByYear(@PathVariable int year) {
        return ResponseEntity.ok(sectionService.getSectionsByYear(year));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SectionResponse>> getSectionsByYear(@PathVariable Long id) {
        return ResponseEntity.ok(sectionService.getSectionById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    public ResponseEntity<ApiResponse<SectionResponse>> saveSection(@Valid @RequestBody SectionRequest sectionRequest) {
        return ResponseEntity.ok(sectionService.saveSection(sectionRequest));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    public ResponseEntity<ApiResponse<SectionResponse>> updateSection(@PathVariable Long id, @Valid @RequestBody SectionRequest sectionRequest) {
        return ResponseEntity.ok(sectionService.updateSection(id, sectionRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    public ResponseEntity<ApiResponse<String>> deleteSection(@PathVariable Long id) {
        ApiResponse<String> response = sectionService.deleteSection(id);
        return ResponseEntity.ok(response);
    }
}
