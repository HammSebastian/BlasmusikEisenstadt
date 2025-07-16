package com.hammsebastian.backend_stadtkapelle_eisenstadt.exception;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiResponse<Map<String, String>>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, WebRequest request) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(
                ApiResponse.<Map<String, String>>builder()
                        .message("Validation failed")
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .errors("Invalid request data")
                        .data(errors)
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateEmail(DuplicateEmailException ex, WebRequest request) {
        return new ResponseEntity<>(
                ApiResponse.<Void>builder()
                        .message(ex.getMessage())
                        .statusCode(HttpStatus.CONFLICT.value())
                        .errors("Email already in use")
                        .build(),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserNotFound(UserNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(
                ApiResponse.<Void>builder()
                        .message(ex.getMessage())
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .errors("User not found")
                        .build(),
                HttpStatus.NOT_FOUND
        );
    }
}
