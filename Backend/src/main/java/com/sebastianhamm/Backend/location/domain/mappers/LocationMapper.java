/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 8/4/25
 */
package com.sebastianhamm.Backend.location.domain.mappers;

import com.sebastianhamm.Backend.location.domain.entities.LocationEntity;
import com.sebastianhamm.Backend.location.api.dtos.LocationRequest;
import com.sebastianhamm.Backend.location.api.dtos.LocationResponse;
import org.springframework.stereotype.Component;

@Component
public class LocationMapper {

    public LocationResponse toResponse(LocationEntity entity) {
        if (entity == null) return null;

        LocationResponse response = new LocationResponse();
        response.setName(entity.getName());
        response.setAddress(entity.getAddress());
        return response;
    }

    public LocationEntity toEntity(LocationRequest request) {
        if (request == null) return null;

        LocationEntity entity = new LocationEntity();
        entity.setName(request.getName());
        entity.setAddress(request.getAddress());
        return entity;
    }

    public void updateEntity(LocationEntity entity, LocationRequest request) {
        if (entity == null || request == null) return;

        entity.setName(request.getName());
        entity.setAddress(request.getAddress());
    }
}