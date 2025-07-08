package at.sebastianhamm.backend.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class AnnouncementsResponse {
    private Long id;
    private String title;
    private String message;
    private Set<String> types;
    private Date startDate;
    private Date endDate;
    private String createdBy;
}
