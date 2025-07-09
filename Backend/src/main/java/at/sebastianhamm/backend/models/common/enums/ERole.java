package at.sebastianhamm.backend.models.common.enums;

/**
 * Enum representing user roles within the application.
 * These roles are designed to be compatible with Spring Security
 * (prefixed with "ROLE_") and define access control levels.
 */
public enum ERole {
    ROLE_MUSICIAN,
    ROLE_SECTION_LEADER,
    ROLE_CONDUCTOR,
    ROLE_REPORTER,
    ROLE_ADMIN
}
