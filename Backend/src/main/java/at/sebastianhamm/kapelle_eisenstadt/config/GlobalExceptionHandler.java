package at.sebastianhamm.kapelle_eisenstadt.config;

import at.sebastianhamm.kapelle_eisenstadt.exception.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("message", ex.getMessage());
        response.put("details", request.getDescription(false));

        //Map custom exceptions to specific http status code
        if(ex instanceof UserAlreadyExistsException) {
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        } else if (ex instanceof BadCredentialsException){
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
