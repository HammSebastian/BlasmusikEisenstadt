/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 7/22/25
 */
package com.hammsebastian.backend_stadtkapelle_eisenstadt.service.impl;


import com.hammsebastian.backend_stadtkapelle_eisenstadt.entity.MemberEntity;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.request.MemberRequest;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.ApiResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.payload.response.MemberResponse;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.repository.MemberRepository;
import com.hammsebastian.backend_stadtkapelle_eisenstadt.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public ApiResponse<List<MemberResponse>> getAllMembers() {
        return ApiResponse.<List<MemberResponse>>builder()
                .message("Members retrieved successfully")
                .statusCode(200)
                .data(memberRepository.findAll().stream()
                        .map(MemberResponse::toMemberResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public ApiResponse<MemberResponse> getMemberById(Long id) {
        if (memberRepository.existsById(id)) {
            return ApiResponse.<MemberResponse>builder()
                    .message("Member retrieved successfully")
                    .statusCode(200)
                    .data(MemberResponse.toMemberResponse(memberRepository.findById(id).get()))
                    .build();
        } else {
            return ApiResponse.<MemberResponse>builder()
                    .message("Member not found")
                    .statusCode(404)
                    .build();
        }
    }

    @Override
    public ApiResponse<MemberResponse> createMember(MemberRequest memberRequest) {
        if (memberRepository.existsMemberEntityByFirstNameAndLastNameAndInstrument(memberRequest.getFirstName(), memberRequest.getLastName(), memberRequest.getInstrument())) {
            return ApiResponse.<MemberResponse>builder()
                    .message("Member already exists")
                    .statusCode(409)
                    .build();
        } else {
            MemberEntity member = MemberEntity.builder()
                    .firstName(memberRequest.getFirstName())
                    .lastName(memberRequest.getLastName())
                    .instrument(memberRequest.getInstrument())
                    .avatarUrl(memberRequest.getAvatarUrl())
                    .dateJoined(memberRequest.getDateJoined())
                    .section(memberRequest.getSection())
                    .notes(memberRequest.getNotes())
                    .dateOfBirth(memberRequest.getDateOfBirth())
                    .phoneNumber(memberRequest.getPhoneNumber())
                    .number(memberRequest.getNumber())
                    .street(memberRequest.getStreet())
                    .zipCode(memberRequest.getZipCode())
                    .city(memberRequest.getCity())
                    .country(memberRequest.getCountry())
                    .build();
            memberRepository.save(member);
            return ApiResponse.<MemberResponse>builder()
                    .message("Member created successfully")
                    .statusCode(201)
                    .data(MemberResponse.toMemberResponse(member))
                    .build();
        }
    }

    @Override
    public ApiResponse<MemberResponse> updateMember(Long id, MemberRequest memberRequest) {
        if (memberRepository.existsById(id)) {
            MemberEntity member = memberRepository.findById(id).get();
            member.setFirstName(memberRequest.getFirstName());
            member.setLastName(memberRequest.getLastName());
            member.setInstrument(memberRequest.getInstrument());
            member.setAvatarUrl(memberRequest.getAvatarUrl());
            member.setDateJoined(memberRequest.getDateJoined());
            member.setSection(memberRequest.getSection());
            member.setNotes(memberRequest.getNotes());
            member.setDateOfBirth(memberRequest.getDateOfBirth());
            member.setPhoneNumber(memberRequest.getPhoneNumber());
            member.setNumber(memberRequest.getNumber());
            member.setStreet(memberRequest.getStreet());
            member.setZipCode(memberRequest.getZipCode());
            member.setCity(memberRequest.getCity());
            member.setCountry(memberRequest.getCountry());
            memberRepository.save(member);
            return ApiResponse.<MemberResponse>builder()
                    .message("Member updated successfully")
                    .statusCode(200)
                    .data(MemberResponse.toMemberResponse(member))
                    .build();
        } else {
            return ApiResponse.<MemberResponse>builder()
                    .message("Member not found")
                    .statusCode(404)
                    .build();
        }
    }

    @Override
    public ApiResponse<String> deleteMember(Long id) {
        if (memberRepository.existsById(id)) {
            memberRepository.deleteById(id);
            return ApiResponse.<String>builder()
                    .message("Member deleted")
                    .statusCode(200)
                    .build();
        } else {
            return ApiResponse.<String>builder()
                    .message("Member not found")
                    .statusCode(404)
                    .build();
        }
    }

    @Override
    public ApiResponse<Long> countAllMembers() {
        return ApiResponse.<Long>builder()
                .message("Member count retrieved successfully")
                .statusCode(200)
                .data(memberRepository.count())
                .build();
    }
}
