package at.sebastianhamm.backend.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GigResponse {
    private Long id;
    private String title;
    private String description;
    private String location;
    private String date;
    private String time;
}
