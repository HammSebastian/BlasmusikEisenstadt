/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/20/25
 */
package com.sebastianhamm.Backend.about.domain.services;

import com.sebastianhamm.Backend.about.api.dtos.AboutRequest;
import com.sebastianhamm.Backend.about.api.dtos.AboutResponse;
import com.sebastianhamm.Backend.shared.api.dtos.ApiResponse;

public interface AboutService {

    ApiResponse<AboutResponse> getAbout();

    ApiResponse<AboutResponse> updateAbout(AboutRequest aboutRequest);
}
