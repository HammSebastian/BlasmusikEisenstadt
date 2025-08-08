/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/28/25
 */
package com.sebastianhamm.Backend.welcome.api.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class WelcomeRequest {

    @NotNull(message = "Title must not be null")
    @Size(max = 500, message = "Title must not exceed 500 characters")
    private String title;

    @NotNull(message = "Subtitle must not be null")
    @Size(max = 1000, message = "Subtitle must not exceed 1000 characters")
    private String subTitle;

    @NotNull(message = "Button text must not be null")
    @Size(max = 255, message = "Button text must not exceed 255 characters")
    private String buttonText;

    @NotNull(message = "Background image URL must not be null")
    @Size(max = 500, message = "Background image URL must not exceed 500 characters")
    private String backgroundImageUrl;

    public WelcomeRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getBackgroundImageUrl() {
        return backgroundImageUrl;
    }

    public void setBackgroundImageUrl(String backgroundImageUrl) {
        this.backgroundImageUrl = backgroundImageUrl;
    }
}
