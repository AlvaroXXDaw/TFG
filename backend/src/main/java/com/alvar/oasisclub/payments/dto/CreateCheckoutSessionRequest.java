package com.alvar.oasisclub.payments.dto;

import com.alvar.oasisclub.reservations.entity.SportType;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import lombok.Data;

@Data
public class CreateCheckoutSessionRequest {

  @NotNull
  private SportType sport;

  @NotNull
  private UUID courtId;

  @NotNull
  private LocalDate date;

  @NotNull
  private LocalTime time;
}
