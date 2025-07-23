/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/20/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.controller;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request.AboutRequest;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.AboutResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ApiResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.service.AboutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/about")
@RequiredArgsConstructor
public class AboutController {

    private final AboutService aboutService;

    @GetMapping
    public ResponseEntity<ApiResponse<AboutResponse>> getAbout() {
        ApiResponse<AboutResponse> response = aboutService.getAbout();

        return response.getData() == null ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResponse.<AboutResponse>builder()
                        .message("About not found")
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .build())
                : ResponseEntity.ok(response);
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    public ResponseEntity<ApiResponse<AboutResponse>> updateAbout(@RequestBody AboutRequest aboutRequest) {
        ApiResponse<AboutResponse> response = aboutService.saveAbout(aboutRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/image")
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    public ResponseEntity<ApiResponse<String>> uploadImage(@RequestParam("file") MultipartFile file) {
        ApiResponse<String> response = aboutService.uploadImage(file);
        return ResponseEntity.ok(response);
    }
}
