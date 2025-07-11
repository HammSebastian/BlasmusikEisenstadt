package at.sebastianhamm.backend.exceptions;

import at.sebastianhamm.backend.payload.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(MethodArgumentNotValidException ex) {
        ApiResponse<?> response = ApiResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Validation failed: " + ex.getBindingResult().getFieldError().getDefaultMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidJson(HttpMessageNotReadableException ex) {
        ApiResponse<?> response = ApiResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Malformed JSON request")
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNoResourceFound(NoResourceFoundException ex) {
        ApiResponse<?> response = ApiResponse.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message("Resource not found")
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNoHandlerFound(NoHandlerFoundException ex) {
        ApiResponse<?> response = ApiResponse.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message("No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL())
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


}