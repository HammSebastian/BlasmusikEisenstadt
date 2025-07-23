/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/20/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class AboutRequest {

    private String aboutText;

    @Column(name = "about_image")
    private String aboutImage;
}
