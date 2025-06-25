package at.sebastianhamm.kapelle_eisenstadt.dto;

import lombok.Data;

@Data
public class ImageResponse {
    private Long id;
    private String imagePath;
    private String imageCategory;
    private String imageAuthor;
}
