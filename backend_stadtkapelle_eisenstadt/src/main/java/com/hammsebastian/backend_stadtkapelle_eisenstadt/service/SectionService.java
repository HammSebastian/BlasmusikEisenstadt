/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/22/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.service;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request.SectionRequest;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ApiResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.SectionResponse;

import java.util.List;

public interface SectionService {

    ApiResponse<List<SectionResponse>> getAllSections();
    ApiResponse<SectionResponse> getSectionById(Long id);

    ApiResponse<SectionResponse> saveSection(SectionRequest sectionRequest);

    ApiResponse<SectionResponse> updateSection(Long id, SectionRequest sectionRequest);

    ApiResponse<String> deleteSection(Long id);

    ApiResponse<List<SectionResponse>> getSectionsByYear(int year);
}
