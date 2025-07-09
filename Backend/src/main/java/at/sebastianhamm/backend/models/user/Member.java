package at.sebastianhamm.backend.models.user;

import at.sebastianhamm.backend.models.remark.Remark;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a member of the organization, possibly a musician.
 * Contains personal details, instrument, section, join date,
 * avatar URL, and a list of associated remarks.
 */
@Entity
@Table(name = "members")
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Member {

    /**
     * Unique identifier for the member.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * Name of the member. Must not be blank.
     */
    @NotBlank
    private String name;

    /**
     * Instrument played by the member. Must not be blank.
     */
    @NotBlank
    private String instrument;

    /**
     * Section the member belongs to (e.g., brass, woodwind). Must not be blank.
     */
    @NotBlank
    private String section;

    /**
     * The date the member joined the organization, as a string.
     * **Consider changing to java.time.LocalDate.**
     */
    @NotBlank
    private String joinDate; // **Vorschlag: Typänderung zu java.time.LocalDate**

    /**
     * URL to the member's avatar image. Can be null.
     */
    private String avatarUrl;

    /**
     * List of remarks associated with this member.
     * Manages the bidirectional relationship with Remark entities.
     * All associated remarks will be cascaded on persistence, merge,
     * remove, refresh, and detach operations. Orphan removal is enabled.
     */
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @ToString.Exclude
    private List<Remark> remarks = new ArrayList<>();

    /**
     * Adds a remark to the list, maintaining bidirectional consistency.
     *
     * @param remark The Remark entity to add.
     */
    public void addRemark(Remark remark) {
        remarks.add(remark);
        remark.setMember(this);
    }

    /**
     * Removes a remark from the list, maintaining bidirectional consistency.
     *
     * @param remark The Remark entity to remove.
     */
    public void removeRemark(Remark remark) {
        remarks.remove(remark);
        remark.setMember(null);
    }
}
