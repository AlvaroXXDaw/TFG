package com.alvar.oasisclub.payments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckoutSessionResponse {
  private String reservationId;
  private String stripeSessionId;
  private String checkoutUrl;
}
