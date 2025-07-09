package at.sebastianhamm.backend.models.common;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Standardized error response DTO (Data Transfer Object) for API error handling.
 * Provides detailed error information following REST best practices,
 * including timestamp, HTTP status, error message, and optional validation details.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    /**
     * Timestamp of the error occurrence in UTC ISO 8601 format.
     * Defaulted to the current instant during object creation.
     */
    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant timestamp = Instant.now();

    /**
     * HTTP status code of the error response (e.g., 400, 404, 500).
     */
    private int status;

    /**
     * Short, human-readable error description (e.g., "Bad Request", "Not Found").
     */
    private String error;

    /**
     * Detailed error message for clients, explaining the issue.
     */
    private String message;

    /**
     * API endpoint path where the error occurred (e.g., "/api/v1/users").
     */
    private String path;

    /**
     * Optional map of field validation errors, where the key is the field name
     * and the value is the validation error message.
     */
    @Builder.Default
    private Map<String, String> validationErrors = new HashMap<>();

    /**
     * Optional internal error code for further diagnostics or client-side specific handling.
     */
    private String errorCode;

    /**
     * Optional URL to documentation providing more details about the error.
     */
    private String documentationUrl;

    /**
     * Adds a single field validation error to the validationErrors map.
     *
     * @param field   The name of the field that failed validation.
     * @param message The validation error message for that field.
     */
    public void addValidationError(String field, String message) {
        validationErrors.put(field, message);
    }
}