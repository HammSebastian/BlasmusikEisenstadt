/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * Exception thrown when a requested resource is not found.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/23/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a requested resource is not found.
 * This exception is used to indicate that a resource (like an entity) could not be found
 * and will be handled by the GlobalExceptionHandler to return a 404 Not Found response.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new ResourceNotFoundException for a resource of the specified type with the specified ID.
     *
     * @param resourceName the name of the resource type
     * @param fieldName the name of the field used to identify the resource
     * @param fieldValue the value of the field used to identify the resource
     * @return a new ResourceNotFoundException with a formatted message
     */
    public static ResourceNotFoundException forResource(String resourceName, String fieldName, Object fieldValue) {
        return new ResourceNotFoundException(
                String.format("%s not found with %s: %s", resourceName, fieldName, fieldValue));
    }
}