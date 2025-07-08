package at.sebastianhamm.backend.payload.response;

import at.sebastianhamm.backend.models.Remark;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

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
