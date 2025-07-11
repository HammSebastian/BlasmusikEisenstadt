package at.sebastianhamm.backend.payload.response;

import at.sebastianhamm.backend.entity.Gig;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class GigResponse {
    private Long id;
    private String title;
    private String description;
    private String type;
    private LocalDate date;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId; // User ID des Gig-Besitzers
    private LocationResponse location;

    public static GigResponse fromGig(Gig gig) {
        return GigResponse.builder()
                .id(gig.getId())
                .title(gig.getTitle())
                .description(gig.getDescription())
                .type(gig.getType().name())
                .date(gig.getDate())
                .createdAt(gig.getCreatedAt())
                .updatedAt(gig.getUpdatedAt())
                .userId(gig.getUser() != null ? gig.getUser().getId() : null) // User ID holen
                .location(LocationResponse.fromLocation(gig.getLocation()))
                .build();
    }
}

