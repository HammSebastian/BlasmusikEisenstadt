package at.sebastianhamm.backend.io;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GigRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Date is required")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate date;

    @NotBlank(message = "Start time is required")
    private LocalTime startTime;

    @NotBlank(message = "End time is required")
    private LocalTime endTime;
}
