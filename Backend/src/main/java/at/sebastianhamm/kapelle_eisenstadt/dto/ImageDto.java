package at.sebastianhamm.kapelle_eisenstadt.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ImageDto {
    private Long id;
    private String path;
    private String category;
    private String author;
}
