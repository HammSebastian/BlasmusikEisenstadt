package at.sebastianhamm.backend.payload.response;

import at.sebastianhamm.backend.enums.ParticipationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ParticipationResponse {

    private Long id;
    private Long userId;
    private Long gigId;
    private Long rehearsalId;
    private ParticipationStatus status;
    private String reason;
    private LocalDateTime respondedAt;

    public static ParticipationResponse fromParticipation(at.sebastianhamm.backend.entity.Participation participation) {
        return ParticipationResponse.builder()
                .id(participation.getId())
                .userId(participation.getUser().getId())
                .gigId(participation.getGig() != null ? participation.getGig().getId() : null)
                .rehearsalId(participation.getRehearsal() != null ? participation.getRehearsal().getId() : null)
                .status(participation.getStatus())
                .reason(participation.getReason())
                .respondedAt(participation.getRespondedAt())
                .build();
    }
}
