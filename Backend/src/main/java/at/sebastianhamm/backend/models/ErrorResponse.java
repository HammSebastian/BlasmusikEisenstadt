package at.sebastianhamm.backend.models;

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
 * Standardized error response DTO for API error handling.
 * Provides detailed error information following REST best practices.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    /**
     * Timestamp of the error occurrence in UTC ISO 8601 format.
     */
    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant timestamp = Instant.now();

    /**
     * HTTP status code of the error response.
     */
    private int status;

    /**
     * Short error description (e.g. "Bad Request").
     */
    private String error;

    /**
     * Detailed error message for clients.
     */
    private String message;

    /**
     * API endpoint path where the error occurred.
     */
    private String path;

    /**
     * Optional map of field validation errors.
     */
    @Builder.Default
    private Map<String, String> validationErrors = new HashMap<>();

    /**
     * Optional internal error code for further diagnostics.
     */
    private String errorCode;

    /**
     * Optional URL to documentation for the error.
     */
    private String documentationUrl;

    /**
     * Adds a single field validation error.
     *
     * @param field   the name of the field
     * @param message the validation error message
     */
    public void addValidationError(String field, String message) {
        validationErrors.put(field, message);
    }
}
