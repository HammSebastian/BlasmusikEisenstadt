package at.sebastianhamm.kapelle_eisenstadt.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class GigDto {
    private Long id;
    private String title;
    private String description;
    private String location;
    private String date;
    private String image;
}
