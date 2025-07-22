/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/20/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.service;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request.WelcomeContentRequest;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ApiResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.WelcomeContentResponse;
import org.springframework.web.multipart.MultipartFile;

public interface WelcomeContentService {

    ApiResponse<WelcomeContentResponse> getSiteContent();

    ApiResponse<WelcomeContentResponse> saveSiteContent(WelcomeContentRequest welcomeContentRequest);

    ApiResponse<String> uploadImage(MultipartFile file);
}
