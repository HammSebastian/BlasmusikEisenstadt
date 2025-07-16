package com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T> {
    private String message;
    private int statusCode;
    private T data;
    private String errors;
}
