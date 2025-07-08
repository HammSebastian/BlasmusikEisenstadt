package at.sebastianhamm.backend.models;

/**
 * Enum representing user roles with prefix ROLE_ for Spring Security compatibility.
 * Immutable list of roles for access control.
 */
public enum ERole {
    ROLE_MUSICIAN,
    ROLE_SECTION_LEADER,
    ROLE_CONDUCTOR,
    ROLE_REPORTER,
    ROLE_ADMIN
}
