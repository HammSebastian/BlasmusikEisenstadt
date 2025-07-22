/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/20/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.controller;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request.WelcomeContentRequest;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ApiResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.WelcomeContentResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.service.WelcomeContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/welcomecontent")
@RequiredArgsConstructor
public class WelcomeContentController {
    private final WelcomeContentService siteContentService;

    @GetMapping
    public ResponseEntity<ApiResponse<WelcomeContentResponse>> getSiteContent() {
        ApiResponse<WelcomeContentResponse> response = siteContentService.getSiteContent();
        if (response.getData() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<WelcomeContentResponse>builder()
                    .message("SiteContent not found")
                    .statusCode(404)
                    .build());
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    public ResponseEntity<ApiResponse<WelcomeContentResponse>> saveSiteContent(@RequestBody WelcomeContentRequest welcomeContentRequest) { // HIER WICHTIG: @RequestBody hinzugefügt
        ApiResponse<WelcomeContentResponse> response = siteContentService.saveSiteContent(welcomeContentRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/upload-image")
    @PreAuthorize("hasAnyAuthority('write:admin', 'write:reporter')")
    public ResponseEntity<ApiResponse<String>> uploadImage(@RequestParam("file") MultipartFile file) {
        ApiResponse<String> reponse = siteContentService.uploadImage(file);
        return ResponseEntity.ok(reponse);
    }
}
