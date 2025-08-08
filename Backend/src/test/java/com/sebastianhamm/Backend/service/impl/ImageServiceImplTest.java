package com.sebastianhamm.Backend.service.impl;

import com.sebastianhamm.Backend.gallery.domain.entities.GalleryEntity;
import com.sebastianhamm.Backend.image.domain.entities.ImageEntity;
import com.sebastianhamm.Backend.image.domain.services.ImageServiceImpl;
import com.sebastianhamm.Backend.shared.api.dtos.ApiResponse;
import com.sebastianhamm.Backend.image.api.dtos.ImageResponse;
import com.sebastianhamm.Backend.gallery.domain.repositories.GalleryRepository;
import com.sebastianhamm.Backend.image.domain.repositories.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ImageServiceImpl.
 * Tests business logic in isolation using mocked dependencies.
 * Note: File operations and security context are mocked for unit testing.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ImageServiceImpl Tests")
class ImageServiceImplTest {

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private GalleryRepository galleryRepository;

    @Mock
    private MultipartFile mockFile;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ImageServiceImpl imageService;

    private ImageEntity testImageEntity;
    private ImageResponse testImageResponse;

    @BeforeEach
    void setUp() {
        // Given - Set up test data
        testImageEntity = new ImageEntity();
        testImageEntity.setId(1L);
        testImageEntity.setFilename("test-image.jpg");
        testImageEntity.setImageUrl("/uploads/test-image.jpg");
        testImageEntity.setMimeType("image/jpeg");
        testImageEntity.setFileSizeBytes(1024L);
        testImageEntity.setSlug("test-context");
        testImageEntity.setAuthor("testuser");
        testImageEntity.setUploadDate(LocalDate.now());
        testImageEntity.setCreatedAt(LocalDateTime.now());

        testImageResponse = new ImageResponse();
        testImageResponse.setId(1L);
        testImageResponse.setFilename("test-image.jpg");
        testImageResponse.setImageUrl("/uploads/test-image.jpg");
        testImageResponse.setMimeType("image/jpeg");
        testImageResponse.setFileSizeBytes(1024L);
        testImageResponse.setAuthor("testuser");
        testImageResponse.setUploadDate(LocalDate.now());
    }

    @Test
    @DisplayName("Should find image by ID successfully")
    void findById_WhenImageExists_ShouldReturnImageResponse() {
        // Given
        when(imageRepository.findById(1L)).thenReturn(Optional.of(testImageEntity));

        // When
        ApiResponse<ImageResponse> result = imageService.findById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(200);
        assertThat(result.getMessage()).isEqualTo("Image found");
        assertThat(result.getData()).isNotNull();
        assertThat(result.getData().getFilename()).isEqualTo("test-image.jpg");
        assertThat(result.getData().getMimeType()).isEqualTo("image/jpeg");

        verify(imageRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should return 404 when image not found by ID")
    void findById_WhenImageNotFound_ShouldReturnNotFoundResponse() {
        // Given
        when(imageRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        ApiResponse<ImageResponse> result = imageService.findById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(404);
        assertThat(result.getMessage()).isEqualTo("Image not found with id: 1");
        assertThat(result.getData()).isNull();

        verify(imageRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should return all images successfully")
    void findAll_WhenImagesExist_ShouldReturnAllImages() {
        // Given
        List<ImageEntity> imageEntities = List.of(testImageEntity);
        when(imageRepository.findAll()).thenReturn(imageEntities);

        // When
        ApiResponse<List<ImageResponse>> result = imageService.findAll();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(200);
        assertThat(result.getMessage()).isEqualTo("Images retrieved successfully");
        assertThat(result.getData()).hasSize(1);
        assertThat(result.getData().get(0).getFilename()).isEqualTo("test-image.jpg");

        verify(imageRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no images exist")
    void findAll_WhenNoImagesExist_ShouldReturnEmptyList() {
        // Given
        when(imageRepository.findAll()).thenReturn(List.of());

        // When
        ApiResponse<List<ImageResponse>> result = imageService.findAll();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(200);
        assertThat(result.getMessage()).isEqualTo("Images retrieved successfully");
        assertThat(result.getData()).isEmpty();

        verify(imageRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should delete image successfully")
    void delete_WhenImageExists_ShouldReturnSuccessResponse() {
        // Given
        when(imageRepository.findById(1L)).thenReturn(Optional.of(testImageEntity));

        // When
        ApiResponse<String> result = imageService.delete(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(200);
        assertThat(result.getMessage()).isEqualTo("Image deleted successfully");
        assertThat(result.getData()).isEqualTo("Image with id: 1 deleted");

        verify(imageRepository, times(1)).findById(1L);
        verify(imageRepository, times(1)).delete(testImageEntity);
    }

    @Test
    @DisplayName("Should return 404 when deleting nonexistent image")
    void delete_WhenImageNotFound_ShouldReturnNotFoundResponse() {
        // Given
        when(imageRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        ApiResponse<String> result = imageService.delete(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(404);
        assertThat(result.getMessage()).isEqualTo("Image not found with id: 1");
        assertThat(result.getData()).isNull();

        verify(imageRepository, times(1)).findById(1L);
        verify(imageRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should find images by slug successfully")
    void findBySlug_WhenImagesExist_ShouldReturnImages() {
        // Given
        GalleryEntity galleryEntity = new GalleryEntity();
        galleryEntity.setImages(List.of(testImageEntity));
        when(galleryRepository.findBySlug("test-context")).thenReturn(List.of(galleryEntity));

        // When
        ApiResponse<List<ImageResponse>> result = imageService.findBySlug("test-context");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(200);
        assertThat(result.getMessage()).isEqualTo("Images found in gallery");
        assertThat(result.getData()).hasSize(1);
        assertThat(result.getData().get(0).getFilename()).isEqualTo("test-image.jpg");

        verify(galleryRepository, times(1)).findBySlug("test-context");
    }

    @Test
    @DisplayName("Should return 404 when no galleries found by slug")
    void findBySlug_WhenNoGalleriesFound_ShouldReturnNotFoundResponse() {
        // Given
        when(galleryRepository.findBySlug("nonexistent-slug")).thenReturn(List.of());

        // When
        ApiResponse<List<ImageResponse>> result = imageService.findBySlug("nonexistent-slug");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(404);
        assertThat(result.getMessage()).isEqualTo("No galleries found with slug: nonexistent-slug");
        assertThat(result.getData()).isNull();

        verify(galleryRepository, times(1)).findBySlug("nonexistent-slug");
    }

    @Test
    @DisplayName("Should return 400 for null slug")
    void findBySlug_WithNullSlug_ShouldReturnBadRequestResponse() {
        // When
        ApiResponse<List<ImageResponse>> result = imageService.findBySlug(null);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(400);
        assertThat(result.getMessage()).isEqualTo("Slug must not be empty");
        assertThat(result.getData()).isNull();

        verify(galleryRepository, never()).findBySlug(any());
    }

    @Test
    @DisplayName("Should return 400 for empty slug")
    void findBySlug_WithEmptySlug_ShouldReturnBadRequestResponse() {
        // When
        ApiResponse<List<ImageResponse>> result = imageService.findBySlug("");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(400);
        assertThat(result.getMessage()).isEqualTo("Slug must not be empty");
        assertThat(result.getData()).isNull();

        verify(galleryRepository, never()).findBySlug(any());
    }

    @Test
    @DisplayName("Should handle repository exception in findAll")
    void findAll_WhenRepositoryThrowsException_ShouldReturnErrorResponse() {
        // Given
        when(imageRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // When
        ApiResponse<List<ImageResponse>> result = imageService.findAll();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(500);
        assertThat(result.getMessage()).isEqualTo("Internal server error");
        assertThat(result.getData()).isNull();

        verify(imageRepository, times(1)).findAll();
    }


    @Test
    @DisplayName("Should handle empty file in saveImage")
    void saveImage_WhenFileIsEmpty_ShouldReturnErrorResponse() {
        // Given
        when(mockFile.isEmpty()).thenReturn(true);

        // When
        ApiResponse<ImageResponse> result = imageService.saveImage(mockFile, "test-context");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(500);
        assertThat(result.getMessage()).isEqualTo("Internal server error");
        assertThat(result.getData()).isNull();

        verify(imageRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should handle null file in saveImage")
    void saveImage_WhenFileIsNull_ShouldReturnErrorResponse() {
        // When
        ApiResponse<ImageResponse> result = imageService.saveImage(null, "test-context");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(500);
        assertThat(result.getMessage()).isEqualTo("Internal server error");
        assertThat(result.getData()).isNull();

        verify(imageRepository, never()).save(any());
    }
}