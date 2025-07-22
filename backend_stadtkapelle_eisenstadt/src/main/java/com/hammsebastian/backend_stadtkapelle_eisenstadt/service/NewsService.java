/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/21/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.service;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request.NewsRequest;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ApiResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.NewsResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NewsService {

    ApiResponse<NewsResponse> getNew(Long id);

    ApiResponse<NewsResponse> saveNews(NewsRequest newsRequest);

    ApiResponse<NewsResponse> updateNews(Long id, NewsRequest newsRequest);

    ApiResponse<String> deleteNews(Long id);

    ApiResponse<String> uploadImage(MultipartFile file);

    ApiResponse<List<NewsResponse>> getAllNews();

}
