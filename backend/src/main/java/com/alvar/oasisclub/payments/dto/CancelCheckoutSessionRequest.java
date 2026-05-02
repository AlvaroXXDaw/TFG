package com.alvar.oasisclub.payments.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CancelCheckoutSessionRequest {

  @NotBlank
  private String stripeSessionId;
}
