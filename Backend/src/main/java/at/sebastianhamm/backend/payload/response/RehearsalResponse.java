package at.sebastianhamm.backend.payload.response;

import at.sebastianhamm.backend.entity.Rehearsal;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class RehearsalResponse {

    private Long id;
    private String title;
    private String description;
    private LocalDate date;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
    private LocationResponse location;

    public static RehearsalResponse fromRehearsal(Rehearsal rehearsal) {
        return RehearsalResponse.builder()
                .id(rehearsal.getId())
                .title(rehearsal.getTitle())
                .description(rehearsal.getDescription())
                .date(rehearsal.getDate())
                .createdAt(rehearsal.getCreatedAt())
                .updatedAt(rehearsal.getUpdatedAt())
                .userId(rehearsal.getUser().getId())
                .location(LocationResponse.fromLocation(rehearsal.getLocation()))
                .build();
    }
}
