package at.sebastianhamm.backend.models.remark;
import at.sebastianhamm.backend.models.user.Member;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a remark or comment associated with a Member.
 * Includes text content, a timestamp, and a type to categorize the remark.
 */
@Entity
@Table(name = "remarks")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Remark {
    /**
     * Unique identifier for the remark.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The textual content of the remark. Must not be blank.
     */
    @NotBlank
    @Column(nullable = false)
    private String text;

    /**
     * Timestamp when the remark was created. Automatically set on creation if not provided.
     */
    @Column(nullable = false)
    private LocalDateTime timestamp;

    /**
     * The type of the remark (e.g., ACCENT, PRIMARY, YELLOW).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RemarkType type;

    /**
     * The Member entity to which this remark belongs.
     * Represents a many-to-one relationship.
     * Uses JsonBackReference to prevent infinite recursion during JSON serialization.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonBackReference
    private Member member;

    /**
     * Constructor for creating a Remark with a specified type.
     * Sets default text based on the type and current timestamp.
     *
     * @param type The RemarkType for this remark.
     */
    public Remark(RemarkType type) {
        this.type = type;
        this.text = type.toString() + " remark"; // Consider making this more flexible or using i18n
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Lifecycle callback method executed before persisting a new entity.
     * Ensures the timestamp is set if it's null.
     */
    @PrePersist
    protected void onCreate() {
        if (this.timestamp == null) {
            this.timestamp = LocalDateTime.now();
        }
    }
}