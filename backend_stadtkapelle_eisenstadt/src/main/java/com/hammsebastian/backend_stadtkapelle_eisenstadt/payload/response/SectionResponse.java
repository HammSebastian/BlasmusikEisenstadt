/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/22/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.entity.SectionEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionResponse {

    private int year;
    private String description;

    public static SectionResponse toSectionResponse(SectionEntity section) {
        return SectionResponse.builder()
                .year(section.getYear())
                .description(section.getDescription())
                .build();
    }
}
