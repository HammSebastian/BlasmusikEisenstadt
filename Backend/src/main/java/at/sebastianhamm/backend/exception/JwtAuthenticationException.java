package at.sebastianhamm.backend.exception;

/**
 * Custom runtime exception for JWT authentication errors.
 */
public class JwtAuthenticationException extends RuntimeException {
    public JwtAuthenticationException(String message) {
        super(message);
    }
}
