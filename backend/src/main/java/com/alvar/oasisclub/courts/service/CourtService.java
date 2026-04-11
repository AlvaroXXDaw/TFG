package com.alvar.oasisclub.courts.service;

import com.alvar.oasisclub.courts.dto.CourtResponse;
import com.alvar.oasisclub.courts.dto.CreateCourtRequest;
import com.alvar.oasisclub.courts.entity.CourtEntity;
import com.alvar.oasisclub.courts.mapper.CourtMapper;
import com.alvar.oasisclub.courts.repository.CourtRepository;
import com.alvar.oasisclub.reservations.entity.SportType;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CourtService {

    private final CourtRepository courtRepository;
    private final CourtMapper courtMapper;

    @Transactional(readOnly = true)
    public List<CourtResponse> getAllCourts(SportType sport) {
        if (sport != null) {
            return courtRepository.findBySportOrderByCreatedAtAsc(sport)
                .stream().map(courtMapper::toResponse).toList();
        }
        return courtRepository.findAll().stream().map(courtMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<CourtResponse> getActiveCourts(SportType sport) {
        if (sport != null) {
            return courtRepository.findBySportAndIsActiveOrderByCreatedAtAsc(sport, true)
                .stream().map(courtMapper::toResponse).toList();
        }
        return courtRepository.findByIsActiveOrderByCreatedAtAsc(true)
            .stream().map(courtMapper::toResponse).toList();
    }

    @Transactional
    public CourtResponse createCourt(CreateCourtRequest request) {
        CourtEntity saved = courtRepository.save(courtMapper.fromCreateRequest(request));
        return courtMapper.toResponse(saved);
    }
    
    @Transactional
    public void deleteCourt(UUID id) {
        CourtEntity court = courtRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Court not found"));

        courtRepository.delete(court);
    }
}

