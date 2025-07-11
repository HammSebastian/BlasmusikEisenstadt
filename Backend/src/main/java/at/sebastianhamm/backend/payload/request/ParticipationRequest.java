package at.sebastianhamm.backend.payload.request;

import at.sebastianhamm.backend.enums.ParticipationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ParticipationRequest {

    private Long userId;

    private Long gigId;        // Entweder GigId

    private Long rehearsalId;  // Oder RehearsalId

    @NotNull(message = "Status cannot be empty")
    private ParticipationStatus status;

    private String reason;
}
