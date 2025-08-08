/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/28/25
 */
package com.sebastianhamm.Backend.member.domain.services;

import com.sebastianhamm.Backend.member.api.dtos.MemberRequest;
import com.sebastianhamm.Backend.shared.api.dtos.ApiResponse;
import com.sebastianhamm.Backend.member.api.dtos.MemberResponse;

import java.util.List;

public interface MemberService {

    ApiResponse<MemberResponse> create(MemberRequest request);
    ApiResponse<MemberResponse> update(Long id, MemberRequest request);
    ApiResponse<MemberResponse> findById(Long id);
    ApiResponse<List<MemberResponse>> findAll();
    ApiResponse<String> delete(Long id);
}
