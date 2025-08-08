/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 8/4/25
 */
package com.sebastianhamm.Backend.about.domain.mappers;

import com.sebastianhamm.Backend.about.domain.entities.AboutEntity;
import com.sebastianhamm.Backend.about.api.dtos.AboutRequest;
import com.sebastianhamm.Backend.about.api.dtos.AboutResponse;
import org.springframework.stereotype.Component;

@Component
public class AboutMapper {

    public AboutResponse toResponse(AboutEntity entity) {
        if (entity == null) return null;

        AboutResponse response = new AboutResponse();
        response.setAboutImageUrl(entity.getAboutImageUrl());
        response.setAboutText(entity.getAboutText());
        return response;
    }

    public AboutEntity toEntity(AboutRequest request) {
        if (request == null) return null;

        AboutEntity entity = new AboutEntity();
        entity.setAboutImageUrl(request.getAboutImageUrl());
        entity.setAboutText(request.getAboutText());
        return entity;
    }

    public void updateEntity(AboutEntity entity, AboutRequest request) {
        if (entity == null || request == null) return;

        entity.setAboutImageUrl(request.getAboutImageUrl());
        entity.setAboutText(request.getAboutText());
    }
}