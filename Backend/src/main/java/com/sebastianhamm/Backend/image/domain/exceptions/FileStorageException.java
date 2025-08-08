/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/28/25
 */
package com.sebastianhamm.Backend.image.domain.exceptions;
import com.sebastianhamm.Backend.gallery.domain.entities.GalleryEntity;


public class FileStorageException extends RuntimeException {
    public FileStorageException(String message) {
        super(message);
    }
}
