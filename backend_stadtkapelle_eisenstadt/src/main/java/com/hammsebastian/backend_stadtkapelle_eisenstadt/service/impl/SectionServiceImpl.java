/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/22/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.service.impl;


import com.hammsebastian.backend_stadtkapelle_eisenstadt.entity.SectionEntity;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request.SectionRequest;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ApiResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.SectionResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.repository.SectionRepository;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.service.SectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService {

    private final SectionRepository sectionRepository;

    @Override
    public ApiResponse<List<SectionResponse>> getAllSections() {
        List<SectionEntity> sections = sectionRepository.findAll();
        List<SectionResponse> sectionResponses = sections.stream()
                .map(SectionResponse::toSectionResponse)
                .collect(Collectors.toList());

        return ApiResponse.<List<SectionResponse>>builder()
                .message("Sections retrieved successfully")
                .statusCode(200)
                .data(sectionResponses)
                .build();
    }

    @Override
    public ApiResponse<SectionResponse> getSectionById(Long id) {
        Optional<SectionEntity> section = sectionRepository.findById(id);

        if (section.isEmpty()) {
            return ApiResponse.<SectionResponse>builder()
                    .message("Section not found")
                    .statusCode(404)
                    .build();
        }

        SectionResponse sectionResponse = SectionResponse.toSectionResponse(section.get());
        return ApiResponse.<SectionResponse>builder()
                .message("Section retrieved successfully")
                .statusCode(200)
                .data(sectionResponse)
                .build();
    }

    @Override
    public ApiResponse<SectionResponse> saveSection(SectionRequest sectionRequest) {
        if (sectionRepository.existsSectionsEntityByYear(sectionRequest.getYear())) {
            return ApiResponse.<SectionResponse>builder()
                    .message("Section with year " + sectionRequest.getYear() + " already exists")
                    .statusCode(409)
                    .build();
        }

        SectionEntity section = new SectionEntity();
        section.setYear(sectionRequest.getYear());
        section.setDescription(sectionRequest.getDescription());
        sectionRepository.save(section);
        return ApiResponse.<SectionResponse>builder()
                .message("Section saved successfully")
                .statusCode(200)
                .data(SectionResponse.toSectionResponse(section))
                .build();
    }

    @Override
    public ApiResponse<SectionResponse> updateSection(Long id, SectionRequest sectionRequest) {
        if (!sectionRepository.existsById(id)) {
            return ApiResponse.<SectionResponse>builder()
                    .message("Section not found")
                    .statusCode(404)
                    .build();
        } else {
            SectionEntity section = sectionRepository.findById(id).get();
            section.setYear(sectionRequest.getYear());
            section.setDescription(sectionRequest.getDescription());
            sectionRepository.save(section);
            return ApiResponse.<SectionResponse>builder()
                    .message("Section updated successfully")
                    .statusCode(200)
                    .data(SectionResponse.toSectionResponse(section))
                    .build();
        }
    }

    @Override
    public ApiResponse<String> deleteSection(Long id) {
        if (sectionRepository.existsById(id)) {
            sectionRepository.deleteById(id);
            return ApiResponse.<String>builder()
                    .message("Section deleted")
                    .statusCode(200)
                    .build();
        } else {
            return ApiResponse.<String>builder()
                    .message("Section not found")
                    .statusCode(404)
                    .build();
        }
    }

    @Override
    public ApiResponse<List<SectionResponse>> getSectionsByYear(int year) {
        List<SectionEntity> sections = sectionRepository.findByYear(year);
        List<SectionResponse> sectionResponses = sections.stream()
                .map(SectionResponse::toSectionResponse)
                .collect(Collectors.toList());

        return ApiResponse.<List<SectionResponse>>builder()
                .message("Sections retrieved successfully")
                .statusCode(200)
                .data(sectionResponses)
                .build();
    }
}
