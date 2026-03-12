package com.alvar.oasisclub.profile.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Data;

@Data
public class ProfileReservationResponse {
  private String id;
  private String sport;
  private String court;
  private LocalDate date;
  private LocalTime time;
  private String status;
}


