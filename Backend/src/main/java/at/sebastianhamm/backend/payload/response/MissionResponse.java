package at.sebastianhamm.backend.payload.response;

import at.sebastianhamm.backend.models.Mission;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MissionResponse {
    private Long id;
    private String title;
    private String description;
    private String imageUrl;

    public static MissionResponse from(Mission mission) {
        if (mission == null) {
            return null;
        }
        return MissionResponse.builder()
                .id(mission.getId())
                .title(mission.getTitle())
                .description(mission.getDescription())
                .imageUrl(mission.getImageUrl())
                .build();
    }
}
