/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/23/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.controller;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request.ImageRequest;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ApiResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ImageResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.service.ImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/{title}")
    public ResponseEntity<ApiResponse<List<ImageResponse>>> getImagesByTitle(@PathVariable String title) {
        return ResponseEntity.ok(imageService.getImagesByGalleryTitle(title));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    public ResponseEntity<ApiResponse<ImageResponse>> saveImage(@Valid @RequestBody ImageRequest imageRequest) {
        return ResponseEntity.ok(imageService.saveImage(imageRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    public ResponseEntity<ApiResponse<String>> deleteImage(@PathVariable Long id) {
        ApiResponse<String> response = imageService.deleteImage(id);
        return ResponseEntity.ok(response);
    }

}
