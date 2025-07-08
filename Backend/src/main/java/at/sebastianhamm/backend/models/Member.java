package at.sebastianhamm.backend.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "members")
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String instrument;

    @NotBlank
    private String section;

    @NotBlank
    private String joinDate;

    private String avatarUrl;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @ToString.Exclude
    private List<Remark> remarks = new ArrayList<>();
    
    public void addRemark(Remark remark) {
        remarks.add(remark);
        remark.setMember(this);
    }
    
    public void removeRemark(Remark remark) {
        remarks.remove(remark);
        remark.setMember(null);
    }
}
