package com.alvar.oasisclub.profile.dto;

import java.util.List;
import lombok.Data;

@Data
public class ProfileResponse {
  private String clientId;
  private String name;
  private List<ProfileReservationResponse> reservations;
}
