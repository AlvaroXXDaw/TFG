package com.alvar.oasisclub.reservations.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AvailabilitySlotResponse {
  private String time;
  private boolean available;
}


