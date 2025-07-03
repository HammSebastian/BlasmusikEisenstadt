package at.sebastianhamm.backend.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GigResponse {
    private Long id;
    private String title;
    private String description;
    private String location;
    private LocalDate date;
    private String time;
}
