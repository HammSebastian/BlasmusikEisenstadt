package at.sebastianhamm.backend.security.jwt;

import at.sebastianhamm.backend.dto.ErrorResponse;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException {
        log.error("Authentication error: {}", ex.getMessage());

        String message = ex.getMessage();
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        String error = "Unauthorized";

        Object jwtErrorObj = request.getAttribute("jwt_error");
        if (jwtErrorObj != null) {
            message = jwtErrorObj.toString().toLowerCase();
            if (message.contains("expired")) {
                error = "Token Expired";
                status = HttpStatus.UNAUTHORIZED;
            } else if (message.contains("unsupported")) {
                error = "Unsupported Token";
                status = HttpStatus.BAD_REQUEST;
            } else if (message.contains("malformed")) {
                error = "Malformed Token";
                status = HttpStatus.BAD_REQUEST;
            } else if (message.contains("signature")) {
                error = "Invalid Token Signature";
                status = HttpStatus.FORBIDDEN;
            }
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error(error)
                .message(message)
                .path(request.getRequestURI())
                .build();

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        response.setHeader("Access-Control-Allow-Origin", String.join(", ", SecurityConstants.ALLOWED_ORIGINS));
        response.setHeader("Access-Control-Allow-Methods", String.join(", ", SecurityConstants.ALLOWED_METHODS));
        response.setHeader("Access-Control-Allow-Headers", String.join(", ", SecurityConstants.ALLOWED_HEADERS));

        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}
