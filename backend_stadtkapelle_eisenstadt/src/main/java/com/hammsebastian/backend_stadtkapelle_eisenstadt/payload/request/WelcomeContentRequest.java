/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/20/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request;


import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WelcomeContentRequest {

    private String title;
    private String subTitle;
    private String buttonText;

    @Column(name = "background_image")
    private String backgroundImage;
}
