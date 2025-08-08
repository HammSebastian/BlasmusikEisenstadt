/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/28/25
 */
package com.sebastianhamm.Backend.member.domain.services;


import com.sebastianhamm.Backend.member.domain.entities.MemberEntity;
import com.sebastianhamm.Backend.shared.domain.entities.Address;
import com.sebastianhamm.Backend.shared.api.dtos.AddressDto;
import com.sebastianhamm.Backend.member.api.dtos.MemberRequest;
import com.sebastianhamm.Backend.shared.api.dtos.ApiResponse;
import com.sebastianhamm.Backend.member.api.dtos.MemberResponse;
import com.sebastianhamm.Backend.member.domain.repositories.MemberRepository;
import com.sebastianhamm.Backend.member.domain.services.MemberService;
import com.sebastianhamm.Backend.member.domain.mappers.MemberMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service

@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    @Override
    public ApiResponse<MemberResponse> create(MemberRequest request) {
        try {
            MemberEntity memberEntity = memberMapper.toEntity(request);
            MemberEntity savedMemberEntity = memberRepository.save(memberEntity);
            return new ApiResponse<>(201, "Member created successfully", memberMapper.toResponse(savedMemberEntity));
        } catch (Exception e) {
            return new ApiResponse<>(500, "Internal server error", null);
        }
    }

    @Override
    public ApiResponse<MemberResponse> update(Long id, MemberRequest request) {
        return memberRepository.findActiveById(id)
                .map(existingMember -> {
                    memberMapper.updateEntity(existingMember, request);
                    existingMember.setUpdatedAt(LocalDateTime.now());
                    MemberEntity updatedMember = memberRepository.save(existingMember);
                    return new ApiResponse<>(200, "Member updated successfully", memberMapper.toResponse(updatedMember));
                })
                .orElseGet(() -> new ApiResponse<>(404, "Member not found with id: " + id, null));
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<MemberResponse> findById(Long id) {
        return memberRepository.findActiveById(id)
                .map(memberEntity -> new ApiResponse<>(200, "Member found", memberMapper.toResponse(memberEntity)))
                .orElseGet(() -> new ApiResponse<>(404, "Member not found with id: " + id, null));
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<MemberResponse>> findAll() {
        try {
            List<MemberResponse> members = memberRepository.findAll()
                    .stream()
                    .map(memberMapper::toResponse)
                    .toList();

            return new ApiResponse<>(200, "Members retrieved successfully", members);
        } catch (Exception e) {
            return new ApiResponse<>(500, "Failed to retrieve members", null);
        }
    }

    @Override
    public ApiResponse<String> delete(Long id) {
        return memberRepository.findActiveById(id)
                .map(memberEntity -> {
                    memberEntity.setDeletedAt(LocalDateTime.now());
                    memberRepository.save(memberEntity);
                    return new ApiResponse<>(204, "Member deleted successfully", "");
                })
                .orElseGet(() -> new ApiResponse<>(404, "Member not found with id: " + id, null));
    }


    public MemberServiceImpl(MemberRepository memberRepository, MemberMapper memberMapper) {
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
    }
}