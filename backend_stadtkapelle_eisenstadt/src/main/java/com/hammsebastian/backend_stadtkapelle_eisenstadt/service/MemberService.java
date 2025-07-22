/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/22/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.service;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request.MemberRequest;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ApiResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.MemberResponse;

import java.util.List;

public interface MemberService {

    ApiResponse<List<MemberResponse>> getAllMembers();

    ApiResponse<MemberResponse> getMemberById(Long id);

    ApiResponse<MemberResponse> createMember(MemberRequest memberRequest);

    ApiResponse<MemberResponse> updateMember(Long id, MemberRequest memberRequest);

    ApiResponse<String> deleteMember(Long id);

    ApiResponse<Long> countAllMembers();
}
