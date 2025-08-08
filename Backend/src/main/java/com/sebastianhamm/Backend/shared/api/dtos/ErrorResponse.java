/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 8/4/25
 */
package com.sebastianhamm.Backend.shared.api.dtos;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse {
    private final int statusCode;
    private final String error;
    private final String message;
    private final String path;
    private final LocalDateTime timestamp;
    private final List<String> details;

    public ErrorResponse(int statusCode, String error, String message, String path) {
        this.statusCode = statusCode;
        this.error = error;
        this.message = message;
        this.path = path;
        this.timestamp = LocalDateTime.now();
        this.details = null;
    }

    public ErrorResponse(int statusCode, String error, String message, String path, List<String> details) {
        this.statusCode = statusCode;
        this.error = error;
        this.message = message;
        this.path = path;
        this.timestamp = LocalDateTime.now();
        this.details = details;
    }

    // Getters
    public int getStatusCode() {
        return statusCode;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public List<String> getDetails() {
        return details;
    }
}