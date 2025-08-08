/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/28/25
 */
package com.sebastianhamm.Backend.welcome.domain.services;

import com.sebastianhamm.Backend.welcome.api.dtos.WelcomeRequest;
import com.sebastianhamm.Backend.shared.api.dtos.ApiResponse;
import com.sebastianhamm.Backend.welcome.api.dtos.WelcomeResponse;

public interface WelcomeService {
    ApiResponse<WelcomeResponse> getWelcome();
    ApiResponse<WelcomeResponse> updateWelcome(WelcomeRequest welcomeRequest);
}
