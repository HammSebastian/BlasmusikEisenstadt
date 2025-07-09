package at.sebastianhamm.backend.models.announcement;

import at.sebastianhamm.backend.models.user.Type;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents an announcement in the system.
 * Contains details such as title, message, associated types,
 * start/end dates, and the creator.
 */
@Entity
@Table(name = "announcements")
@Data
@RequiredArgsConstructor
public class Announcement {

    /**
     * Unique identifier for the announcement.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Title of the announcement. Must not be blank.
     */
    @NotBlank
    private String title;

    /**
     * The main message content of the announcement. Must not be blank.
     */
    @NotBlank
    private String message;

    /**
     * A set of types associated with this announcement.
     * Represents a many-to-many relationship with Type entities.
     * Types are lazily loaded.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "announcement_types",
            joinColumns = @JoinColumn(name = "announcement_id"),
            inverseJoinColumns = @JoinColumn(name = "type_id"))
    private Set<Type> types = new HashSet<>();

    /**
     * The date from which the announcement is active.
     */
    private Date startDate;

    /**
     * The date until which the announcement is active.
     */
    private Date endDate;

    /**
     * The name or identifier of the user who created the announcement. Must not be blank.
     */
    @NotBlank
    private String createdBy;

    /**
     * Constructor for creating an Announcement with essential details.
     *
     * @param title     The title of the announcement.
     * @param message   The message content of the announcement.
     * @param types     A set of Type entities associated with the announcement.
     * @param startDate The start date of the announcement's validity.
     * @param createdBy The identifier of the creator.
     */
    public Announcement(String title, String message, Set<Type> types, Date startDate, String createdBy) {
        this.title = title;
        this.message = message;
        this.types = types;
        this.startDate = startDate;
        this.createdBy = createdBy;
    }
}