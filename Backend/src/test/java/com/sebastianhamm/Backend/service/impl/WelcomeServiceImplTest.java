package com.sebastianhamm.Backend.service.impl;

import com.sebastianhamm.Backend.welcome.domain.entities.WelcomeEntity;
import com.sebastianhamm.Backend.welcome.api.dtos.WelcomeRequest;
import com.sebastianhamm.Backend.shared.api.dtos.ApiResponse;
import com.sebastianhamm.Backend.welcome.api.dtos.WelcomeResponse;
import com.sebastianhamm.Backend.welcome.domain.repositories.WelcomeRepository;
import com.sebastianhamm.Backend.welcome.domain.services.WelcomeServiceImpl;
import com.sebastianhamm.Backend.welcome.domain.mappers.WelcomeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doThrow;

/**
 * Unit tests for WelcomeServiceImpl.
 * Tests business logic in isolation using mocked dependencies.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("WelcomeServiceImpl Tests")
class WelcomeServiceImplTest {

    @Mock
    private WelcomeRepository welcomeRepository;

    @Mock
    private WelcomeMapper welcomeMapper;

    @InjectMocks
    private WelcomeServiceImpl welcomeService;

    private WelcomeEntity testWelcomeEntity;
    private WelcomeRequest testWelcomeRequest;

    @BeforeEach
    void setUp() {
        // Given - Set up test data
        testWelcomeEntity = new WelcomeEntity();
        testWelcomeEntity.setId(1L);
        testWelcomeEntity.setTitle("Welcome to Our Band");
        testWelcomeEntity.setSubTitle("Experience the magic of music");
        testWelcomeEntity.setButtonText("Learn More");
        testWelcomeEntity.setBackgroundImageUrl("https://example.com/background.jpg");
        testWelcomeEntity.setUpdatedAt(LocalDateTime.of(2025, 1, 1, 12, 0));

        testWelcomeRequest = new WelcomeRequest();
        testWelcomeRequest.setTitle("Updated Welcome Title");
        testWelcomeRequest.setSubTitle("Updated subtitle");
        testWelcomeRequest.setButtonText("Updated Button");
        testWelcomeRequest.setBackgroundImageUrl("https://example.com/new-background.jpg");
    }

    @Test
    @DisplayName("Should return welcome data successfully when entity exists")
    void getWelcome_WhenEntityExists_ShouldReturnSuccessResponse() {
        // Given
        WelcomeResponse expectedResponse = new WelcomeResponse();
        expectedResponse.setTitle("Welcome to Our Band");
        expectedResponse.setSubTitle("Experience the magic of music");
        expectedResponse.setButtonText("Learn More");
        expectedResponse.setBackgroundImageUrl("https://example.com/background.jpg");
        expectedResponse.setUpdatedAt(LocalDateTime.of(2025, 1, 1, 12, 0));
        
        when(welcomeRepository.getById(1L)).thenReturn(testWelcomeEntity);
        when(welcomeMapper.toResponse(testWelcomeEntity)).thenReturn(expectedResponse);

        // When
        ApiResponse<WelcomeResponse> result = welcomeService.getWelcome();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(200);
        assertThat(result.getMessage()).isEqualTo("Welcome data retrieved successfully");
        assertThat(result.getData()).isNotNull();
        assertThat(result.getData().getTitle()).isEqualTo("Welcome to Our Band");
        assertThat(result.getData().getSubTitle()).isEqualTo("Experience the magic of music");
        assertThat(result.getData().getButtonText()).isEqualTo("Learn More");
        assertThat(result.getData().getBackgroundImageUrl()).isEqualTo("https://example.com/background.jpg");
        assertThat(result.getData().getUpdatedAt()).isEqualTo(LocalDateTime.of(2025, 1, 1, 12, 0));

        verify(welcomeRepository, times(1)).getById(1L);
    }

    @Test
    @DisplayName("Should return error response when entity does not exist")
    void getWelcome_WhenEntityDoesNotExist_ShouldReturnErrorResponse() {
        // Given
        when(welcomeRepository.getById(1L)).thenReturn(null);

        // When
        ApiResponse<WelcomeResponse> result = welcomeService.getWelcome();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(500);
        assertThat(result.getMessage()).isEqualTo("Internal Server Error");
        assertThat(result.getData()).isNull();

        verify(welcomeRepository, times(1)).getById(1L);
    }

    @Test
    @DisplayName("Should return welcome data when updating (current implementation)")
    void updateWelcome_WhenEntityExists_ShouldReturnCurrentDataWithNewTimestamp() {
        // Given
        WelcomeResponse expectedResponse = new WelcomeResponse();
        expectedResponse.setTitle("Updated Welcome Title");
        expectedResponse.setSubTitle("Updated subtitle");
        expectedResponse.setButtonText("Updated Button");
        expectedResponse.setBackgroundImageUrl("https://example.com/new-background.jpg");
        expectedResponse.setUpdatedAt(LocalDateTime.now());
        
        when(welcomeRepository.getById(1L)).thenReturn(testWelcomeEntity);
        when(welcomeMapper.toResponse(testWelcomeEntity)).thenReturn(expectedResponse);

        // When
        ApiResponse<WelcomeResponse> result = welcomeService.updateWelcome(testWelcomeRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(200);
        assertThat(result.getMessage()).isEqualTo("Welcome data updated successfully");
        assertThat(result.getData()).isNotNull();
        assertThat(result.getData().getTitle()).isEqualTo("Updated Welcome Title");
        assertThat(result.getData().getSubTitle()).isEqualTo("Updated subtitle");
        assertThat(result.getData().getButtonText()).isEqualTo("Updated Button");
        assertThat(result.getData().getBackgroundImageUrl()).isEqualTo("https://example.com/new-background.jpg");

        verify(welcomeRepository, times(1)).getById(1L);
        verify(welcomeMapper, times(1)).updateEntity(testWelcomeEntity, testWelcomeRequest);
        verify(welcomeMapper, times(1)).toResponse(testWelcomeEntity);
    }

    @Test
    @DisplayName("Should return error response when updating non-existent entity")
    void updateWelcome_WhenEntityDoesNotExist_ShouldReturnErrorResponse() {
        // Given
        when(welcomeRepository.getById(1L)).thenReturn(null);

        // When
        ApiResponse<WelcomeResponse> result = welcomeService.updateWelcome(testWelcomeRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(400);
        assertThat(result.getMessage()).isEqualTo("Welcome data not found");
        assertThat(result.getData()).isNull();

        verify(welcomeRepository, times(1)).getById(1L);
    }

    @Test
    @DisplayName("Should handle repository exception gracefully")
    void updateWelcome_WhenRepositoryThrowsException_ShouldReturnErrorResponse() {
        // Given
        when(welcomeRepository.getById(1L)).thenThrow(new RuntimeException("Database connection failed"));

        // When
        ApiResponse<WelcomeResponse> result = welcomeService.updateWelcome(testWelcomeRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(500);
        assertThat(result.getMessage()).startsWith("Internal server error:");
        assertThat(result.getMessage()).contains("Database connection failed");
        assertThat(result.getData()).isNull();

        verify(welcomeRepository, times(1)).getById(1L);
    }

    @Test
    @DisplayName("Should handle null request gracefully")
    void updateWelcome_WithNullRequest_ShouldStillWork() {
        // Given
        WelcomeResponse expectedResponse = new WelcomeResponse();
        expectedResponse.setTitle("Welcome to Our Band");
        expectedResponse.setSubTitle("Experience the magic of music");
        expectedResponse.setButtonText("Learn More");
        expectedResponse.setBackgroundImageUrl("https://example.com/background.jpg");
        expectedResponse.setUpdatedAt(LocalDateTime.now());
        
        when(welcomeRepository.getById(1L)).thenReturn(testWelcomeEntity);
        when(welcomeMapper.toResponse(testWelcomeEntity)).thenReturn(expectedResponse);

        // When
        ApiResponse<WelcomeResponse> result = welcomeService.updateWelcome(null);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(200);
        assertThat(result.getMessage()).isEqualTo("Welcome data updated successfully");
        assertThat(result.getData()).isNotNull();

        verify(welcomeRepository, times(1)).getById(1L);
        verify(welcomeMapper, times(1)).updateEntity(testWelcomeEntity, null);
        verify(welcomeMapper, times(1)).toResponse(testWelcomeEntity);
    }
}