package com.alvar.oasisclub.reservations.dto;

import com.alvar.oasisclub.reservations.entity.SportType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Data;

@Data
public class CreateReservationRequest {

  private String clientId;

  @NotBlank
  private String userName;

  @NotNull
  private SportType sport;

  @NotNull
  private java.util.UUID courtId;

  @NotNull
  private LocalDate date;

  @NotNull
  private LocalTime time;
}


