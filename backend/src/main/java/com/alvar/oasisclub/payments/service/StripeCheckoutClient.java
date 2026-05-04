package com.alvar.oasisclub.payments.service;

import com.alvar.oasisclub.clients.entity.ClientEntity;
import com.alvar.oasisclub.reservations.entity.ReservationEntity;
import com.stripe.exception.StripeException;

public interface StripeCheckoutClient {

  StripeCheckoutSession createReservationCheckoutSession(
      ReservationEntity reservation,
      ClientEntity client,
      long amountInCents,
      String successUrl,
      String cancelUrl
  ) throws StripeException;

  void expireSession(String stripeSessionId) throws StripeException;
}
