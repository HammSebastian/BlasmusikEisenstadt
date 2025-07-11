package at.sebastianhamm.backend.payload.request;


import at.sebastianhamm.backend.enums.Type;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GigRequest {

    @NotBlank(message = "Title cannot be empty")
    @Size(max = 200, message = "Title cannot exceed 200 characters")
    private String title;

    @NotBlank(message = "Description cannot be empty")
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @Enumerated(EnumType.STRING)
    private Type type;

    @FutureOrPresent(message = "Date must be in the future or present")
    private LocalDate date;

    @NotNull(message = "Location ID cannot be empty")
    private Long locationId;
}
