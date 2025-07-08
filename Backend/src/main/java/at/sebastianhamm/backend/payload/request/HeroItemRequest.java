package at.sebastianhamm.backend.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class HeroItemRequest {
    private String title;
    private String description;
    private String imageUrl;
}
