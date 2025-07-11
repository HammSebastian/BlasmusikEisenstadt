package at.sebastianhamm.backend.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ApiResponse<T> {
    private int statusCode;
    private String message;
    private T data;

    @Builder(builderMethodName = "genericBuilder")
    public ApiResponse(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }
}
