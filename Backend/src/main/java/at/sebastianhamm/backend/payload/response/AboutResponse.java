package at.sebastianhamm.backend.payload.response;

import lombok.*;

import java.util.Set;

/**
 * Response DTO for About information.
 * Contains only mission titles as strings for security reasons.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AboutResponse {
    private Long id;
    private String imageUrl;
    private String story;
    private Set<MissionResponse> missions;  // kein Set<String> mehr
}


