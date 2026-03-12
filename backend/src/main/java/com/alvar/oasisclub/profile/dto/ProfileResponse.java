package com.alvar.oasisclub.profile.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class ProfileResponse {
  private String clientId;
  private String name;
  private String subscriptionName;
  private LocalDate nextBillingDate;
  private Integer subscriptionAmountCents;
  private List<ProfileReservationResponse> reservations;
}


