/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 8/4/25
 */
package com.sebastianhamm.Backend.welcome.domain.mappers;

import com.sebastianhamm.Backend.welcome.domain.entities.WelcomeEntity;
import com.sebastianhamm.Backend.welcome.api.dtos.WelcomeRequest;
import com.sebastianhamm.Backend.welcome.api.dtos.WelcomeResponse;
import org.springframework.stereotype.Component;

@Component
public class WelcomeMapper {

    public WelcomeResponse toResponse(WelcomeEntity entity) {
        if (entity == null) return null;

        WelcomeResponse response = new WelcomeResponse();
        response.setTitle(entity.getTitle());
        response.setSubTitle(entity.getSubTitle());
        response.setButtonText(entity.getButtonText());
        response.setBackgroundImageUrl(entity.getBackgroundImageUrl());
        response.setUpdatedAt(entity.getUpdatedAt());
        return response;
    }

    public WelcomeEntity toEntity(WelcomeRequest request) {
        if (request == null) return null;

        WelcomeEntity entity = new WelcomeEntity();
        entity.setTitle(request.getTitle());
        entity.setSubTitle(request.getSubTitle());
        entity.setButtonText(request.getButtonText());
        entity.setBackgroundImageUrl(request.getBackgroundImageUrl());
        return entity;
    }

    public void updateEntity(WelcomeEntity entity, WelcomeRequest request) {
        if (entity == null || request == null) return;

        entity.setTitle(request.getTitle());
        entity.setSubTitle(request.getSubTitle());
        entity.setButtonText(request.getButtonText());
        entity.setBackgroundImageUrl(request.getBackgroundImageUrl());
    }
}