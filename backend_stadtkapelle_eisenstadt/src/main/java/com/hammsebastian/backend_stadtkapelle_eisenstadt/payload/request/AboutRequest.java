/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 * <p>
 * Represents the request payload for creating or updating the 'About' section content.
 *
 * @author Sebastian Hamm
 * @version 1.1.0
 * @since 7/20/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@Builder
public class AboutRequest {

    /**
     * The main descriptive text for the "About Us" section. Cannot be blank.
     */
    @NotBlank(message = "About text must not be blank.")
    @Size(max = 1000, message = "About text must not exceed 1000 characters.")
    private String aboutText;

    /**
     * The URL of the primary image for the "About Us" section. Must be a valid URL.
     */
    @URL(message = "About image must be a valid URL.")
    @Size(max = 500, message = "Image URL must not exceed 500 characters.")
    private String aboutImageUrl;
}