/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/22/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.service.impl;


import com.hammsebastian.backend_stadtkapelle_eisenstadt.entity.HistoryEntity;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.entity.SectionsEntity;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request.HistoryRequest;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ApiResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.HistoryResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.repository.HistoryRepository;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.repository.SectionRepository;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;
    private final SectionRepository sectionRepository;

    @Override
    public ApiResponse<HistoryResponse> getHistory() {
        Optional<HistoryEntity> historyEntity = historyRepository.findById(1L);

        if (historyEntity.isEmpty()) {
            return ApiResponse.<HistoryResponse>builder()
                    .message("History not found")
                    .statusCode(404)
                    .build();
        }
        return ApiResponse.<HistoryResponse>builder()
                .message("History found")
                .statusCode(200)
                .data(HistoryResponse.toHistoryResponse(historyEntity.get()))
                .build();
    }

    @Override
    public ApiResponse<HistoryResponse> saveHistory(HistoryRequest historyRequest) {
        Optional<HistoryEntity> historyEntity = historyRepository.findById(1L);
        Optional<SectionsEntity> sectionEntity = sectionRepository.findById(1L);

        if (sectionEntity.isEmpty()) {
            return ApiResponse.<HistoryResponse>builder()
                    .message("Section not found")
                    .statusCode(404)
                    .build();
        }

        if (historyEntity.isEmpty()) {
            HistoryEntity history = HistoryEntity.builder()
                    .name(historyRequest.getName())
                    .sections(sectionEntity.get().getHistory().getSections())
                    .build();
            historyRepository.save(history);

            return ApiResponse.<HistoryResponse>builder()
                    .message("History saved/updated successfully")
                    .statusCode(200)
                    .data(HistoryResponse.toHistoryResponse(history))
                    .build();
        } else {
            return ApiResponse.<HistoryResponse>builder()
                    .message("History already exists")
                    .statusCode(409)
                    .build();
        }
    }
}
