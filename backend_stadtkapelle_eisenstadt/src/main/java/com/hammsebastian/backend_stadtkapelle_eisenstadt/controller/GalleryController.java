/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/23/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.controller;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request.GalleryRequest;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ApiResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.GalleryResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.service.GalleryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gallery")
@RequiredArgsConstructor
public class GalleryController {

    private final GalleryService galleryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<GalleryResponse>>> getAllGalleries() {
        return ResponseEntity.ok(galleryService.getAllGalleries());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GalleryResponse>> getGallery(@PathVariable Long id) {
        return ResponseEntity.ok(galleryService.getGalleryById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    public ResponseEntity<ApiResponse<GalleryResponse>> saveGallery(@Valid @RequestBody GalleryRequest galleryRequest) {
        return ResponseEntity.ok(galleryService.createGallery(galleryRequest));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    public ResponseEntity<ApiResponse<GalleryResponse>> updateGallery(@PathVariable Long id, @Valid @RequestBody GalleryRequest galleryRequest) {
        return ResponseEntity.ok(galleryService.updateGallery(id, galleryRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    public ResponseEntity<ApiResponse<String>> deleteGallery(@PathVariable Long id) {
        ApiResponse<String> response = galleryService.deleteGallery(id);
        return ResponseEntity.ok(response);
    }
}
