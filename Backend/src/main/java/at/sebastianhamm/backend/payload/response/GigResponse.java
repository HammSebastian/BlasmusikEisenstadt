package at.sebastianhamm.backend.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.sql.Time;
import java.util.Date;
import java.util.Set;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class GigResponse {
    private Long id;
    private String title;
    private String description;
    private String venue;
    private String address;
    private String imageUrl;
    private String note;
    private Date date;
    private Time time;
    private String createdBy;
    private Set<String> types;
}
