package at.sebastianhamm.backend.payload.response;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResponse {
    private Long id;
    private String name;
    private String instrument;
    private String section;
    private String joinDate;
    private String avatarUrl;
    private Set<RemarkResponse> remarks;
}
