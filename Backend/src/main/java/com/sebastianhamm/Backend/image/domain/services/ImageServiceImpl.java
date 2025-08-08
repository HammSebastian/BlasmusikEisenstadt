/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/28/25
 */
package com.sebastianhamm.Backend.image.domain.services;
import com.sebastianhamm.Backend.gallery.domain.entities.GalleryEntity;


import com.sebastianhamm.Backend.image.domain.entities.ImageEntity;
import com.sebastianhamm.Backend.image.domain.mappers.ImageMapper;
import com.sebastianhamm.Backend.shared.api.dtos.ApiResponse;
import com.sebastianhamm.Backend.image.api.dtos.ImageResponse;
import com.sebastianhamm.Backend.gallery.domain.repositories.GalleryRepository;
import com.sebastianhamm.Backend.image.domain.repositories.ImageRepository;
import com.sebastianhamm.Backend.image.domain.services.ImageService;

import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
@Transactional
public class ImageServiceImpl implements ImageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);
    
    private final ImageRepository imageRepository;
    private final GalleryRepository galleryRepository;
    private final ImageMapper imageMapper;
    private final Tika tika = new Tika();

    private static final List<String> ALLOWED_TYPES = List.of("image/jpeg", "image/png", "image/webp");
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB

    @Override
    public ApiResponse<ImageResponse> saveImage(MultipartFile file, String context) {
        try {
            String author = getCurrentUsername();
            validateFile(file);
            String mimeType = detectMimeType(file);
            validateMimeType(mimeType);

            Optional<ImageEntity> imageEntityOpt = mapToEntity(file, author, context, mimeType);
            if (imageEntityOpt.isEmpty()) {
                return new ApiResponse<>(500, "Internal server error", null);
            }
            ImageEntity savedImage = imageRepository.save(imageEntityOpt.get());
            return new ApiResponse<>(201, "Image uploaded successfully", ImageMapper.toResponse(savedImage));

        } catch (IllegalArgumentException | IOException e) {
            logger.error("Failed to process image upload: {}", e.getMessage(), e);
            return new ApiResponse<>(500, "Internal server error", null);
        }
    }

    @Override
    public ApiResponse<ImageResponse> updateImage(Long id, MultipartFile file, String context) {
        try {
            String author = getCurrentUsername();
            validateFile(file);
            String mimeType = detectMimeType(file);
            validateMimeType(mimeType);

            return imageRepository.findById(id)
                    .map(existingImage -> {
                        Optional<String> stored = storeFile(file, context);
                        if (stored.isEmpty()) {
                            return new ApiResponse<ImageResponse>(500, "Internal server error", null);
                        }
                        existingImage.setAuthor(author);
                        existingImage.setImageUrl(stored.get());
                        existingImage.setFilename(file.getOriginalFilename());
                        existingImage.setMimeType(mimeType);
                        existingImage.setFileSizeBytes(file.getSize());
                        existingImage.setUploadDate(LocalDate.now());

                        ImageEntity updatedImage = imageRepository.save(existingImage);
                        return new ApiResponse<>(200, "Image updated successfully", ImageMapper.toResponse(updatedImage));
                    })
                    .orElseGet(() -> new ApiResponse<>(404, "Image not found with id: " + id, null));

        } catch (IllegalArgumentException | IOException e) {
            logger.error("Failed to update image with id {}: {}", id, e.getMessage(), e);
            return new ApiResponse<>(500, "Internal server error", null);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<ImageResponse> findById(Long id) {
        return imageRepository.findById(id)
                .map(imageEntity -> new ApiResponse<>(200, "Image found", ImageMapper.toResponse(imageEntity)))
                .orElseGet(() -> new ApiResponse<>(404, "Image not found with id: " + id, null));
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<ImageResponse>> findAll() {
        try {
            List<ImageResponse> images = imageRepository.findAll()
                    .stream()
                    .map(ImageMapper::toResponse)
                    .toList();

            return new ApiResponse<>(200, "Images retrieved successfully", images);

        } catch (Exception e) {
            logger.error("Failed to retrieve all images: {}", e.getMessage(), e);
            return new ApiResponse<>(500, "Internal server error", null);
        }
    }

    @Override
    public ApiResponse<String> delete(Long id) {
        return imageRepository.findById(id)
                .map(imageEntity -> {
                    imageRepository.delete(imageEntity);
                    return new ApiResponse<>(200, "Image deleted successfully", "Image with id: " + id + " deleted");
                })
                .orElseGet(() -> new ApiResponse<>(404, "Image not found with id: " + id, null));
    }

    @Override
    public ApiResponse<List<ImageResponse>> findBySlug(String slug) {
        if (slug == null || slug.isEmpty()) {
            return new ApiResponse<>(400, "Slug must not be empty", null);
        }

        List<GalleryEntity> galleryEntities = galleryRepository.findBySlug(slug);
        if (galleryEntities == null || galleryEntities.isEmpty()) {
            return new ApiResponse<>(404, "No galleries found with slug: " + slug, null);
        }

        GalleryEntity gallery = galleryEntities.get(0);
        List<ImageEntity> images = gallery.getImages();
        if (images == null || images.isEmpty()) {
            return new ApiResponse<>(404, "No images found in gallery with slug: " + slug, null);
        }
        List<ImageResponse> responseList = ImageMapper.toResponseList(images);
        return new ApiResponse<>(200, "Images found in gallery", responseList);
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File must not be empty");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size exceeds max allowed size");
        }
    }

    private String detectMimeType(MultipartFile file) throws IOException {
        return tika.detect(file.getInputStream());
    }

    private void validateMimeType(String mimeType) {
        if (mimeType == null || !mimeType.startsWith("image/")) {
            throw new IllegalArgumentException("Unsupported file type");
        }
    }

    private String getCurrentUsername() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return auth.getName();
        }
        return "anonymous";
    }

    private Optional<String> storeFile(MultipartFile file, String context) {
        if (file == null || file.isEmpty()) return Optional.empty();

        String contentType = file.getContentType();
        if (!ALLOWED_TYPES.contains(contentType)) return Optional.empty();

        if (file.getSize() > MAX_FILE_SIZE) return Optional.empty();

        try {
            String extension = getExtension(file.getOriginalFilename());
            String filename = UUID.randomUUID() + "." + extension;
            Path storageDir = Paths.get("uploads/images/" + context).toAbsolutePath().normalize();
            Files.createDirectories(storageDir);

            Path targetPath = storageDir.resolve(filename);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            return Optional.of("/uploads/images/" + context + "/" + filename);

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Optional<ImageEntity> mapToEntity(MultipartFile file, String author, String context, String mimeType) {
        Optional<String> stored = storeFile(file, context);
        if (stored.isEmpty()) {
            return Optional.empty();
        }

        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setAuthor(author);
        imageEntity.setUploadDate(LocalDate.now());
        imageEntity.setImageUrl(stored.get());
        imageEntity.setFilename(file.getOriginalFilename());
        imageEntity.setMimeType(mimeType);
        imageEntity.setFileSizeBytes(file.getSize());
        imageEntity.setSlug(generateSlug(file, context));
        return Optional.of(imageEntity);
    }

    private String generateSlug(MultipartFile file, String context) {
        String original = file.getOriginalFilename() != null ? file.getOriginalFilename() : UUID.randomUUID().toString();
        String slug = original.replaceAll("\\s+", "-")
                .replaceAll("[^a-zA-Z0-9\\-]", "")
                .toLowerCase();
        return context + "-" + slug;
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            // Kein Exception-Throw mehr, sondern leeres Optional oder Fehler-Handling wo aufgerufen
            return "";
        }
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    }


    public ImageServiceImpl(ImageRepository imageRepository, GalleryRepository galleryRepository, ImageMapper imageMapper) {
        this.imageRepository = imageRepository;
        this.galleryRepository = galleryRepository;
        this.imageMapper = imageMapper;
    }
}