package com.alvar.oasisclub.reservations.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Data;

@Data
public class ReservationResponse {
  private String id;
  private String clientId;
  private String userName;
  private String sport;
  private String court;
  private LocalDate date;
  private LocalTime time;
  private String status;
  private String stripeSessionId;
}


