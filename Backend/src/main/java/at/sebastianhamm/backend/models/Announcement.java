package at.sebastianhamm.backend.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "announcements")
@Data
@RequiredArgsConstructor
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String message;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "announcement_types",
            joinColumns = @JoinColumn(name = "announcement_id"),
            inverseJoinColumns = @JoinColumn(name = "type_id"))
    private Set<Type> types = new HashSet<>();

    private Date startDate;

    private Date endDate;

    @NotBlank
    private String createdBy;

    public Announcement(String title, String message, Set<Type> types, Date startDate, String createdBy) {
        this.title = title;
        this.message = message;
        this.types = types;
        this.startDate = startDate;
        this.createdBy = createdBy;
    }
}