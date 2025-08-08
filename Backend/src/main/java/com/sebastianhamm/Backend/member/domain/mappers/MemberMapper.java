/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 8/4/25
 */
package com.sebastianhamm.Backend.member.domain.mappers;

import com.sebastianhamm.Backend.member.domain.entities.MemberEntity;
import com.sebastianhamm.Backend.shared.domain.entities.Address;
import com.sebastianhamm.Backend.shared.api.dtos.AddressDto;
import com.sebastianhamm.Backend.member.api.dtos.MemberRequest;
import com.sebastianhamm.Backend.member.api.dtos.MemberResponse;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

    public MemberResponse toResponse(MemberEntity entity) {
        if (entity == null) return null;

        MemberResponse response = new MemberResponse();
        response.setId(entity.getId());
        response.setFirstName(entity.getFirstName());
        response.setLastName(entity.getLastName());
        response.setInstrument(entity.getInstrument());
        response.setAvatarUrl(entity.getAvatarUrl());
        response.setDateJoined(entity.getDateJoined());
        response.setSection(entity.getSection());
        response.setNotes(entity.getNotes());
        response.setPhoneNumber(entity.getPhoneNumber());
        response.setAddress(mapAddressEntityToDto(entity.getAddress()));
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());

        return response;
    }

    public MemberEntity toEntity(MemberRequest request) {
        if (request == null) return null;

        MemberEntity entity = new MemberEntity();
        entity.setFirstName(request.getFirstName());
        entity.setLastName(request.getLastName());
        entity.setInstrument(request.getInstrument());
        entity.setAvatarUrl(request.getAvatarUrl());
        entity.setDateJoined(request.getDateJoined());
        entity.setSection(request.getSection());
        entity.setNotes(request.getNotes());
        entity.setPhoneNumber(request.getPhoneNumber());
        entity.setAddress(mapAddressDtoToEntity(request.getAddress()));
        return entity;
    }

    public void updateEntity(MemberEntity entity, MemberRequest request) {
        if (entity == null || request == null) return;

        entity.setFirstName(request.getFirstName());
        entity.setLastName(request.getLastName());
        entity.setInstrument(request.getInstrument());
        entity.setAvatarUrl(request.getAvatarUrl());
        entity.setDateJoined(request.getDateJoined());
        entity.setSection(request.getSection());
        entity.setNotes(request.getNotes());
        entity.setPhoneNumber(request.getPhoneNumber());
        entity.setAddress(mapAddressDtoToEntity(request.getAddress()));
    }

    private AddressDto mapAddressEntityToDto(Address address) {
        if (address == null) return null;

        AddressDto dto = new AddressDto();
        dto.setStreet(address.getStreet());
        dto.setStreetNumber(address.getStreetNumber());
        dto.setCity(address.getCity());
        dto.setPostalNumber(address.getPostalCode());
        dto.setCountry(address.getCountry());

        return dto;
    }

    private Address mapAddressDtoToEntity(AddressDto dto) {
        if (dto == null) return null;
        
        Address address = new Address();
        address.setStreet(dto.getStreet());
        address.setStreetNumber(dto.getStreetNumber());
        address.setPostalCode(dto.getPostalNumber());
        address.setCity(dto.getCity());
        address.setCountry(dto.getCountry());
        return address;
    }
}