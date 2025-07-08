package at.sebastianhamm.backend.payload.response;

import at.sebastianhamm.backend.models.Remark;
import at.sebastianhamm.backend.models.RemarkType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RemarkResponse {
    private Long id;
    private String text;
    private LocalDateTime timestamp;
    private RemarkType type;

    public RemarkResponse(RemarkType remark) {
        this.type = remark;
    }

    public static RemarkResponse from(Remark remark) {
        if (remark == null) return null;
        return RemarkResponse.builder()
                .id(remark.getId())
                .text(remark.getText())
                .timestamp(remark.getTimestamp())
                .type(RemarkType.valueOf(remark.getType().name()))
                .build();
    }
}
