package com.alvar.oasisclub.courts.mapper;

import com.alvar.oasisclub.courts.dto.CourtResponse;
import com.alvar.oasisclub.courts.dto.CreateCourtRequest;
import com.alvar.oasisclub.courts.entity.CourtEntity;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class CourtMapper {

    public CourtResponse toResponse(CourtEntity entity) {
        CourtResponse response = new CourtResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setSport(entity.getSport());
        response.setIsActive(entity.getIsActive());
        response.setCreatedAt(entity.getCreatedAt());
        return response;
    }

    public CourtEntity fromCreateRequest(CreateCourtRequest request) {
        CourtEntity entity = new CourtEntity();
        entity.setId(UUID.randomUUID());
        entity.setName(request.getName());
        entity.setSport(request.getSport());
        return entity;
    }
}

