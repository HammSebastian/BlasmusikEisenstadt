package com.sebastianhamm.Backend.service.impl;

import com.sebastianhamm.Backend.about.domain.entities.AboutEntity;
import com.sebastianhamm.Backend.about.api.dtos.AboutRequest;
import com.sebastianhamm.Backend.about.api.dtos.AboutResponse;
import com.sebastianhamm.Backend.about.domain.services.AboutServiceImpl;
import com.sebastianhamm.Backend.shared.api.dtos.ApiResponse;
import com.sebastianhamm.Backend.about.domain.repositories.AboutRepository;
import com.sebastianhamm.Backend.about.domain.mappers.AboutMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

/**
 * Unit tests for AboutServiceImpl.
 * Tests business logic in isolation using mocked dependencies.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AboutServiceImpl Tests")
class AboutServiceImplTest {

    @Mock
    private AboutRepository aboutRepository;

    @Mock
    private AboutMapper aboutMapper;

    @InjectMocks
    private AboutServiceImpl aboutService;

    private AboutEntity testAboutEntity;
    private AboutRequest testAboutRequest;

    @BeforeEach
    void setUp() {
        // Given - Set up test data
        testAboutEntity = new AboutEntity();
        testAboutEntity.setId(1L);
        testAboutEntity.setAboutText("Test about text");
        testAboutEntity.setAboutImageUrl("https://example.com/image.jpg");

        testAboutRequest = new AboutRequest();
        testAboutRequest.setAboutText("Updated about text");
        testAboutRequest.setAboutImageUrl("https://example.com/updated-image.jpg");
    }

    @Test
    @DisplayName("Should return about data successfully when entity exists")
    void getAbout_WhenEntityExists_ShouldReturnSuccessResponse() {
        // Given
        AboutResponse expectedResponse = new AboutResponse();
        expectedResponse.setAboutText("Test about text");
        expectedResponse.setAboutImageUrl("https://example.com/image.jpg");
        
        when(aboutRepository.getById(1L)).thenReturn(testAboutEntity);
        when(aboutMapper.toResponse(testAboutEntity)).thenReturn(expectedResponse);

        // When
        ApiResponse<AboutResponse> result = aboutService.getAbout();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(200);
        assertThat(result.getMessage()).isEqualTo("About data successfully fetched");
        assertThat(result.getData()).isNotNull();
        assertThat(result.getData().getAboutText()).isEqualTo("Test about text");
        assertThat(result.getData().getAboutImageUrl()).isEqualTo("https://example.com/image.jpg");

        verify(aboutRepository, times(1)).getById(1L);
    }

    @Test
    @DisplayName("Should return error response when entity does not exist")
    void getAbout_WhenEntityDoesNotExist_ShouldReturnErrorResponse() {
        // Given
        when(aboutRepository.getById(1L)).thenReturn(null);

        // When
        ApiResponse<AboutResponse> result = aboutService.getAbout();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(500);
        assertThat(result.getMessage()).isEqualTo("Internal Server Error");
        assertThat(result.getData()).isNull();

        verify(aboutRepository, times(1)).getById(1L);
    }

    @Test
    @DisplayName("Should return about data when updating (current implementation)")
    void updateAbout_WhenEntityExists_ShouldReturnCurrentData() {
        // Given
        AboutResponse expectedResponse = new AboutResponse();
        expectedResponse.setAboutText("Updated about text");
        expectedResponse.setAboutImageUrl("https://example.com/updated-image.jpg");
        
        when(aboutRepository.getById(1L)).thenReturn(testAboutEntity);
        when(aboutRepository.save(testAboutEntity)).thenReturn(testAboutEntity);
        when(aboutMapper.toResponse(testAboutEntity)).thenReturn(expectedResponse);

        // When
        ApiResponse<AboutResponse> result = aboutService.updateAbout(testAboutRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(200);
        assertThat(result.getMessage()).isEqualTo("About data successfully updated");
        assertThat(result.getData()).isNotNull();
        assertThat(result.getData().getAboutText()).isEqualTo("Updated about text");
        assertThat(result.getData().getAboutImageUrl()).isEqualTo("https://example.com/updated-image.jpg");

        verify(aboutRepository, times(1)).getById(1L);
        verify(aboutMapper, times(1)).updateEntity(testAboutEntity, testAboutRequest);
        verify(aboutRepository, times(1)).save(testAboutEntity);
        verify(aboutMapper, times(1)).toResponse(testAboutEntity);
    }

    @Test
    @DisplayName("Should return error response when updating non-existent entity")
    void updateAbout_WhenEntityDoesNotExist_ShouldReturnErrorResponse() {
        // Given
        when(aboutRepository.getById(1L)).thenReturn(null);

        // When
        ApiResponse<AboutResponse> result = aboutService.updateAbout(testAboutRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(500);
        assertThat(result.getMessage()).isEqualTo("Internal Server Error");
        assertThat(result.getData()).isNull();

        verify(aboutRepository, times(1)).getById(1L);
    }

    @Test
    @DisplayName("Should handle null request gracefully")
    void updateAbout_WithNullRequest_ShouldStillWork() {
        // Given
        AboutResponse expectedResponse = new AboutResponse();
        expectedResponse.setAboutText("Test about text");
        expectedResponse.setAboutImageUrl("https://example.com/image.jpg");
        
        when(aboutRepository.getById(1L)).thenReturn(testAboutEntity);
        when(aboutRepository.save(testAboutEntity)).thenReturn(testAboutEntity);
        when(aboutMapper.toResponse(testAboutEntity)).thenReturn(expectedResponse);

        // When
        ApiResponse<AboutResponse> result = aboutService.updateAbout(null);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(200);
        assertThat(result.getMessage()).isEqualTo("About data successfully updated");
        assertThat(result.getData()).isNotNull();

        verify(aboutRepository, times(1)).getById(1L);
        verify(aboutMapper, times(1)).updateEntity(testAboutEntity, null);
        verify(aboutRepository, times(1)).save(testAboutEntity);
        verify(aboutMapper, times(1)).toResponse(testAboutEntity);
    }
}