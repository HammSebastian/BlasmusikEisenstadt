package com.sebastianhamm.Backend.service.impl;

import com.sebastianhamm.Backend.gallery.domain.entities.GalleryEntity;
import com.sebastianhamm.Backend.gallery.domain.services.GalleryServiceImpl;
import com.sebastianhamm.Backend.image.domain.entities.ImageEntity;
import com.sebastianhamm.Backend.gallery.domain.mappers.GalleryMapper;
import com.sebastianhamm.Backend.gallery.api.dtos.GalleryRequest;
import com.sebastianhamm.Backend.shared.api.dtos.ApiResponse;
import com.sebastianhamm.Backend.gallery.api.dtos.GalleryResponse;
import com.sebastianhamm.Backend.gallery.domain.repositories.GalleryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import org.mockito.MockedStatic;

/**
 * Unit tests for GalleryServiceImpl.
 * Tests business logic in isolation using mocked dependencies.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GalleryServiceImpl Tests")
class GalleryServiceImplTest {

    @Mock
    private GalleryRepository galleryRepository;

    @Mock
    private GalleryMapper galleryMapper;

    @InjectMocks
    private GalleryServiceImpl galleryService;

    private GalleryEntity testGalleryEntity;
    private GalleryRequest testGalleryRequest;
    private GalleryResponse testGalleryResponse;

    @BeforeEach
    void setUp() {
        // Given - Set up test data
        testGalleryEntity = new GalleryEntity();
        testGalleryEntity.setId(1L);
        testGalleryEntity.setTitle("Test Gallery");
        testGalleryEntity.setGalleryDate(LocalDate.of(2025, 1, 15));
        testGalleryEntity.setCreatedAt(LocalDateTime.of(2025, 1, 1, 10, 0));
        testGalleryEntity.setUpdatedAt(LocalDateTime.of(2025, 1, 1, 10, 0));
        testGalleryEntity.setImages(new ArrayList<>());

        testGalleryRequest = new GalleryRequest();
        testGalleryRequest.setTitle("Test Gallery");
        testGalleryRequest.setGalleryDate(LocalDate.of(2025, 1, 15));

        testGalleryResponse = new GalleryResponse();
        testGalleryResponse.setId(1L);
        testGalleryResponse.setTitle("Test Gallery");
        testGalleryResponse.setGalleryDate(LocalDate.of(2025, 1, 15));
    }

    @Test
    @DisplayName("Should create gallery successfully")
    void create_WhenValidRequest_ShouldReturnCreatedResponse() {
        // Given
        try (MockedStatic<GalleryMapper> mockedStatic = mockStatic(GalleryMapper.class)) {
            mockedStatic.when(() -> GalleryMapper.toEntity(testGalleryRequest)).thenReturn(testGalleryEntity);
            when(galleryRepository.save(testGalleryEntity)).thenReturn(testGalleryEntity);
            when(galleryMapper.toResponse(testGalleryEntity)).thenReturn(testGalleryResponse);

            // When
            ApiResponse<GalleryResponse> result = galleryService.create(testGalleryRequest);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getStatusCode()).isEqualTo(201);
            assertThat(result.getMessage()).isEqualTo("Gallery created successfully");
            assertThat(result.getData()).isNotNull();
            assertThat(result.getData().getTitle()).isEqualTo("Test Gallery");
            assertThat(result.getData().getGalleryDate()).isEqualTo(LocalDate.of(2025, 1, 15));

            verify(galleryRepository, times(1)).save(testGalleryEntity);
            verify(galleryMapper, times(1)).toResponse(testGalleryEntity);
        }
    }

    @Test
    @DisplayName("Should handle exception during gallery creation")
    void create_WhenRepositoryThrowsException_ShouldReturnErrorResponse() {
        // Given
        when(galleryRepository.save(any(GalleryEntity.class))).thenThrow(new RuntimeException("Database error"));

        // When
        ApiResponse<GalleryResponse> result = galleryService.create(testGalleryRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(500);
        assertThat(result.getMessage()).isEqualTo("Internal server error");
        assertThat(result.getData()).isNull();

        verify(galleryRepository, times(1)).save(any(GalleryEntity.class));
    }

    @Test
    @DisplayName("Should update gallery successfully")
    void update_WhenGalleryExists_ShouldReturnUpdatedResponse() {
        // Given
        GalleryRequest updateRequest = new GalleryRequest();
        updateRequest.setTitle("Updated Gallery");
        updateRequest.setGalleryDate(LocalDate.of(2025, 2, 15));

        when(galleryRepository.findById(1L)).thenReturn(Optional.of(testGalleryEntity));
        when(galleryRepository.save(testGalleryEntity)).thenReturn(testGalleryEntity);

        // When
        ApiResponse<GalleryResponse> result = galleryService.update(1L, updateRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(200);
        assertThat(result.getMessage()).isEqualTo("Gallery updated successfully");
        assertThat(result.getData()).isNotNull();
        assertThat(result.getData().getTitle()).isEqualTo("Updated Gallery");
        assertThat(result.getData().getGalleryDate()).isEqualTo(LocalDate.of(2025, 2, 15));

        verify(galleryRepository, times(1)).findById(1L);
        verify(galleryRepository, times(1)).save(testGalleryEntity);
        // Verify that the entity was updated
        assertThat(testGalleryEntity.getTitle()).isEqualTo("Updated Gallery");
        assertThat(testGalleryEntity.getGalleryDate()).isEqualTo(LocalDate.of(2025, 2, 15));
        assertThat(testGalleryEntity.getUpdatedAt()).isAfter(LocalDateTime.of(2025, 1, 1, 10, 0));
    }

    @Test
    @DisplayName("Should return 404 when updating nonexistent gallery")
    void update_WhenGalleryNotFound_ShouldReturnNotFoundResponse() {
        // Given
        when(galleryRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        ApiResponse<GalleryResponse> result = galleryService.update(1L, testGalleryRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(404);
        assertThat(result.getMessage()).isEqualTo("Gallery not found with id: 1");
        assertThat(result.getData()).isNull();

        verify(galleryRepository, times(1)).findById(1L);
        verify(galleryRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should find gallery by ID successfully")
    void findById_WhenGalleryExists_ShouldReturnGallery() {
        // Given
        when(galleryRepository.findById(1L)).thenReturn(Optional.of(testGalleryEntity));
        when(galleryMapper.toResponse(testGalleryEntity)).thenReturn(testGalleryResponse);

        // When
        ApiResponse<GalleryResponse> result = galleryService.findById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(200);
        assertThat(result.getMessage()).isEqualTo("Gallery found");
        assertThat(result.getData()).isNotNull();
        assertThat(result.getData().getTitle()).isEqualTo("Test Gallery");
        assertThat(result.getData().getGalleryDate()).isEqualTo(LocalDate.of(2025, 1, 15));

        verify(galleryRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should return 404 when gallery not found by ID")
    void findById_WhenGalleryNotFound_ShouldReturnNotFoundResponse() {
        // Given
        when(galleryRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        ApiResponse<GalleryResponse> result = galleryService.findById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(404);
        assertThat(result.getMessage()).isEqualTo("Gallery not found with id: 1");
        assertThat(result.getData()).isNull();

        verify(galleryRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should return all galleries successfully")
    void findAll_WhenGalleriesExist_ShouldReturnAllGalleries() {
        // Given
        List<GalleryEntity> galleryEntities = List.of(testGalleryEntity);
        when(galleryRepository.findAll()).thenReturn(galleryEntities);
        when(galleryMapper.toResponse(testGalleryEntity)).thenReturn(testGalleryResponse);

        // When
        ApiResponse<List<GalleryResponse>> result = galleryService.findAll();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(200);
        assertThat(result.getMessage()).isEqualTo("Galleries retrieved successfully");
        assertThat(result.getData()).hasSize(1);
        assertThat(result.getData().get(0).getTitle()).isEqualTo("Test Gallery");

        verify(galleryRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should handle exception during findAll")
    void findAll_WhenRepositoryThrowsException_ShouldReturnErrorResponse() {
        // Given
        when(galleryRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // When
        ApiResponse<List<GalleryResponse>> result = galleryService.findAll();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(500);
        assertThat(result.getMessage()).isEqualTo("Internal server error");
        assertThat(result.getData()).isNull();

        verify(galleryRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should soft delete gallery successfully")
    void delete_WhenGalleryExists_ShouldReturnSuccessResponse() {
        // Given
        when(galleryRepository.findById(1L)).thenReturn(Optional.of(testGalleryEntity));
        when(galleryRepository.save(testGalleryEntity)).thenReturn(testGalleryEntity);

        // When
        ApiResponse<String> result = galleryService.delete(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(204);
        assertThat(result.getMessage()).isEqualTo("Gallery deleted successfully");
        assertThat(result.getData()).isEqualTo("");

        verify(galleryRepository, times(1)).findById(1L);
        verify(galleryRepository, times(1)).save(testGalleryEntity);
        // Verify that deletedAt was set
        assertThat(testGalleryEntity.getDeletedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should return 404 when deleting nonexistent gallery")
    void delete_WhenGalleryNotFound_ShouldReturnNotFoundResponse() {
        // Given
        when(galleryRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        ApiResponse<String> result = galleryService.delete(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(404);
        assertThat(result.getMessage()).isEqualTo("Gallery not found with id: 1");
        assertThat(result.getData()).isNull();

        verify(galleryRepository, times(1)).findById(1L);
        verify(galleryRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should handle null request gracefully in create")
    void create_WithNullRequest_ShouldReturnErrorResponse() {
        // When
        ApiResponse<GalleryResponse> result = galleryService.create(null);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(500);
        assertThat(result.getMessage()).isEqualTo("Internal server error");
        assertThat(result.getData()).isNull();

        // Verify that repository was not called due to null pointer exception
        verify(galleryRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should return empty list when no galleries exist")
    void findAll_WhenNoGalleriesExist_ShouldReturnEmptyList() {
        // Given
        when(galleryRepository.findAll()).thenReturn(List.of());

        // When
        ApiResponse<List<GalleryResponse>> result = galleryService.findAll();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(200);
        assertThat(result.getMessage()).isEqualTo("Galleries retrieved successfully");
        assertThat(result.getData()).isEmpty();

        verify(galleryRepository, times(1)).findAll();
    }
}