package at.sebastianhamm.backend.model;

public enum Role {
    USER,
    ADMIN,
    MODERATOR;

    @Override
    public String toString() {
        return "ROLE_" + name();
    }
}
