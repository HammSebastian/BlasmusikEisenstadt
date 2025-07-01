package at.sebastianhamm.backend.security.jwt;

import at.sebastianhamm.backend.dto.ErrorResponse;
import at.sebastianhamm.backend.exception.JwtAuthenticationException;
import at.sebastianhamm.backend.security.SecurityConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {
        log.error("Authentication error: {}", authException.getMessage());

        HttpStatus status = HttpStatus.UNAUTHORIZED;
        String error = "Unauthorized";
        String message = authException.getMessage();
        String path = request.getRequestURI();

        // Handle specific JWT exceptions
        if (request.getAttribute("jwt_error") != null) {
            message = request.getAttribute("jwt_error").toString();
            
            if (message.contains("expired")) {
                status = HttpStatus.UNAUTHORIZED;
                error = "Token Expired";
            } else if (message.contains("unsupported")) {
                status = HttpStatus.BAD_REQUEST;
                error = "Unsupported Token";
            } else if (message.contains("malformed")) {
                status = HttpStatus.BAD_REQUEST;
                error = "Malformed Token";
            } else if (message.contains("signature")) {
                status = HttpStatus.FORBIDDEN;
                error = "Invalid Token Signature";
            }
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error(error)
                .message(message)
                .path(path)
                .build();

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        
        // Add CORS headers
        response.setHeader("Access-Control-Allow-Origin", String.join(", ", SecurityConstants.ALLOWED_ORIGINS));
        response.setHeader("Access-Control-Allow-Methods", String.join(", ", SecurityConstants.ALLOWED_METHODS));
        response.setHeader("Access-Control-Allow-Headers", String.join(", ", SecurityConstants.ALLOWED_HEADERS));
        
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}
