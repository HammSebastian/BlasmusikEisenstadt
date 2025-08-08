/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/28/25
 */
package com.sebastianhamm.Backend.about.domain.services;

import com.sebastianhamm.Backend.about.domain.entities.AboutEntity;
import com.sebastianhamm.Backend.about.api.dtos.AboutRequest;
import com.sebastianhamm.Backend.about.api.dtos.AboutResponse;
import com.sebastianhamm.Backend.shared.api.dtos.ApiResponse;
import com.sebastianhamm.Backend.about.domain.repositories.AboutRepository;
import com.sebastianhamm.Backend.about.domain.mappers.AboutMapper;
import org.springframework.stereotype.Service;

@Service
public class AboutServiceImpl implements AboutService {

    private final AboutRepository aboutRepository;
    private final AboutMapper aboutMapper;

    @Override
    public ApiResponse<AboutResponse> getAbout() {

        AboutEntity aboutEntity = aboutRepository.getById(1L);

        if (aboutEntity == null) {
            return new ApiResponse<>(500, "Internal Server Error", null);
        }

        AboutResponse aboutResponse = aboutMapper.toResponse(aboutEntity);

        return new ApiResponse<>(200, "About data successfully fetched", aboutResponse);
    }

    @Override
    public ApiResponse<AboutResponse> updateAbout(AboutRequest aboutRequest) {
        AboutEntity aboutEntity = aboutRepository.getById(1L);

        if (aboutEntity == null) {
            return new ApiResponse<>(500, "Internal Server Error", null);
        }

        aboutMapper.updateEntity(aboutEntity, aboutRequest);
        AboutEntity updatedEntity = aboutRepository.save(aboutEntity);
        AboutResponse aboutResponse = aboutMapper.toResponse(updatedEntity);

        return new ApiResponse<>(200, "About data successfully updated", aboutResponse);
    }

    public AboutServiceImpl(AboutRepository aboutRepository, AboutMapper aboutMapper) {
        this.aboutRepository = aboutRepository;
        this.aboutMapper = aboutMapper;
    }
}
