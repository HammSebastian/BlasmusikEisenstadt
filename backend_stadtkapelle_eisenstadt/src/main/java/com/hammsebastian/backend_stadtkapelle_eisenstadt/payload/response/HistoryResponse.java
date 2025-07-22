/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/22/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response;


import com.hammsebastian.backend_stadtkapelle_eisenstadt.entity.HistoryEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class HistoryResponse {

    private String name;
    private List<SectionResponse> sections;

    public static HistoryResponse toHistoryResponse(HistoryEntity history) {
        List<SectionResponse> sectionResponses = history.getSections().stream()
                .map(SectionResponse::toSectionResponse)
                .collect(Collectors.toList());

        return HistoryResponse.builder()
                .name(history.getName())
                .sections(sectionResponses)
                .build();
    }

}
