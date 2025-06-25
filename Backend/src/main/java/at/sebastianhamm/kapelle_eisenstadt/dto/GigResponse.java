package at.sebastianhamm.kapelle_eisenstadt.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class GigResponse {
    private Long id;
    private String gigTitel;
    private String gigDescription;
    private String gigLocation;
    private LocalDate gigDate;
}
