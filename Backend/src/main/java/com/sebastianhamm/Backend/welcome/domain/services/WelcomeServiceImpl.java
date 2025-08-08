/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/28/25
 */
package com.sebastianhamm.Backend.welcome.domain.services;


import com.sebastianhamm.Backend.welcome.domain.entities.WelcomeEntity;
import com.sebastianhamm.Backend.welcome.api.dtos.WelcomeRequest;
import com.sebastianhamm.Backend.shared.api.dtos.ApiResponse;
import com.sebastianhamm.Backend.welcome.api.dtos.WelcomeResponse;
import com.sebastianhamm.Backend.welcome.domain.repositories.WelcomeRepository;
import com.sebastianhamm.Backend.welcome.domain.services.WelcomeService;
import com.sebastianhamm.Backend.welcome.domain.mappers.WelcomeMapper;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service

public class WelcomeServiceImpl implements WelcomeService {

    private final WelcomeRepository welcomeRepository;
    private final WelcomeMapper welcomeMapper;

    @Override
    public ApiResponse<WelcomeResponse> getWelcome() {
        WelcomeEntity welcomeEntity = welcomeRepository.getById(1L);

        if (welcomeEntity == null) {
            return new ApiResponse<>(500, "Internal Server Error", null);
        }

        WelcomeResponse welcomeResponse = welcomeMapper.toResponse(welcomeEntity);

        return new ApiResponse<>(200, "Welcome data retrieved successfully", welcomeResponse);
    }

    @Override
    public ApiResponse<WelcomeResponse> updateWelcome(WelcomeRequest welcomeRequest) {
        try {
            WelcomeEntity welcomeEntity = welcomeRepository.getById(1L);

            if (welcomeEntity == null) {
                return new ApiResponse<>(400, "Welcome data not found", null);
            }

            welcomeMapper.updateEntity(welcomeEntity, welcomeRequest);
            welcomeEntity.setUpdatedAt(LocalDateTime.now());
            WelcomeResponse response = welcomeMapper.toResponse(welcomeEntity);

            return new ApiResponse<>(200, "Welcome data updated successfully", response);
        } catch (Exception e) {
            return new ApiResponse<>(500, "Internal server error: " + e.getMessage(), null);
        }
    }

    public WelcomeServiceImpl(WelcomeRepository welcomeRepository, WelcomeMapper welcomeMapper) {
        this.welcomeRepository = welcomeRepository;
        this.welcomeMapper = welcomeMapper;
    }
}
