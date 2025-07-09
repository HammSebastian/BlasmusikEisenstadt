package at.sebastianhamm.backend.payload.response;

import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class GigResponse {
    private Long id;
    private String title;
    private String description;
    private String venue;
    private String address;
    private String imageUrl;
    private String additionalInfo;
    private LocalDate date;
    private LocalTime time;
    private Set<String> types;

    @PrePersist
    private void prePersist() {
        if (date == null) {
            date = LocalDate.now();
        }
        if (time == null) {
            time = LocalTime.now();
        }
    }
}
