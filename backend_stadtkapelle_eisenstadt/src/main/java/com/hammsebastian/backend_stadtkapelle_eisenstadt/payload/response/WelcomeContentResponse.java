/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/20/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WelcomeContentResponse {
    private String title;
    private String subTitle;
    private String buttonText;
    private String backgroundImage;
}
