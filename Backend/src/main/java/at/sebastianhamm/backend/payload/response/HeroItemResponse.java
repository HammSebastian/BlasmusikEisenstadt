package at.sebastianhamm.backend.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class HeroItemResponse {
    private String title;
    private String description;
    private String imageUrl;
}