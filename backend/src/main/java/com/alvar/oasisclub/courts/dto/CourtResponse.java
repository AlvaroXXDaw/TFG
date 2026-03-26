package com.alvar.oasisclub.courts.dto;

import com.alvar.oasisclub.reservations.entity.SportType;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class CourtResponse {
    private UUID id;
    private String name;
    private SportType sport;
    private Boolean isActive;
    private LocalDateTime createdAt;
}

