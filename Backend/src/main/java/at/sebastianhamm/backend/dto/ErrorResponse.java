package at.sebastianhamm.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder(builderClassName = "ErrorResponseBuilder")
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private Map<String, String> validationErrors;
    private String errorCode;
    private String documentationUrl;

    public void addValidationError(String field, String message) {
        if (validationErrors == null) {
            validationErrors = new HashMap<>();
        }
        validationErrors.put(field, message);
    }

    public static ErrorResponseBuilder builder() {
        return new ErrorResponseBuilder()
                .timestamp(Instant.now());
    }

    public static class ErrorResponseBuilder {
        private Instant timestamp = Instant.now();
        private int status;
        private String error;
        private String message;
        private String path;
        private Map<String, String> validationErrors = new HashMap<>();
        private String errorCode;
        private String documentationUrl;

        public ErrorResponseBuilder status(int status) {
            this.status = status;
            return this;
        }

        public ErrorResponseBuilder error(String error) {
            this.error = error;
            return this;
        }

        public ErrorResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ErrorResponseBuilder path(String path) {
            this.path = path;
            return this;
        }

        public ErrorResponseBuilder validationError(String field, String message) {
            this.validationErrors.put(field, message);
            return this;
        }

        public ErrorResponseBuilder validationErrors(Map<String, String> validationErrors) {
            if (validationErrors != null) {
                this.validationErrors = validationErrors;
            }
            return this;
        }

        public ErrorResponseBuilder errorCode(String errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public ErrorResponseBuilder documentationUrl(String documentationUrl) {
            this.documentationUrl = documentationUrl;
            return this;
        }
        
        public ErrorResponseBuilder timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public ErrorResponse build() {
            ErrorResponse response = new ErrorResponse();
            response.setTimestamp(this.timestamp);
            response.setStatus(this.status);
            response.setError(this.error);
            response.setMessage(this.message);
            response.setPath(this.path);
            response.setValidationErrors(this.validationErrors);
            response.setErrorCode(this.errorCode);
            response.setDocumentationUrl(this.documentationUrl);
            return response;
        }
    }
}
