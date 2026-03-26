package com.alvar.oasisclub.courts.dto;

import com.alvar.oasisclub.reservations.entity.SportType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCourtRequest {
    @NotBlank
    private String name;
    
    @NotNull
    private SportType sport;
}

