package at.sebastianhamm.backend.security;

public final class SecurityConstants {
    // JWT
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String REFRESH_TOKEN_COOKIE = "refreshToken";
    public static final String JWT_COOKIE = "jwt";

    // Roles
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_EDITOR = "EDITOR";
    public static final String ROLE_USER = "USER";

    // Security
    public static final long JWT_EXPIRATION_MS = 86400000; // 24 hours
    public static final long REFRESH_TOKEN_EXPIRATION_MS = 604800000; // 7 days
    public static final int MAX_LOGIN_ATTEMPTS = 5;
    public static final int ACCOUNT_LOCK_DURATION_MINUTES = 30;

    // CORS
    public static final String[] ALLOWED_ORIGINS = {
            "http://localhost:4200",
            "http://localhost:3000",
            "https://blasmusik-eisenstadt.at"
    };

    public static final String[] ALLOWED_METHODS = {
            "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
    };

    public static final String[] ALLOWED_HEADERS = {
            "Authorization", "Content-Type", "X-Auth-Token", "X-Requested-With"
    };

    public static final String[] EXPOSED_HEADERS = {
            "X-Auth-Token"
    };


    private SecurityConstants() {
        // Private constructor to prevent instantiation
    }
}
