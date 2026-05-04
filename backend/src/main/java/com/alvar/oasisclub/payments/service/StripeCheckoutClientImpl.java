package com.alvar.oasisclub.payments.service;

import com.alvar.oasisclub.clients.entity.ClientEntity;
import com.alvar.oasisclub.reservations.entity.ReservationEntity;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.net.RequestOptions;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StripeCheckoutClientImpl implements StripeCheckoutClient {

  private final String secretKey;

  public StripeCheckoutClientImpl(@Value("${app.stripe.secret-key:}") String secretKey) {
    this.secretKey = secretKey;
  }

  @Override
  public StripeCheckoutSession createReservationCheckoutSession(
      ReservationEntity reservation,
      ClientEntity client,
      long amountInCents,
      String successUrl,
      String cancelUrl
  ) throws StripeException {
    RequestOptions requestOptions = requestOptions();
    String reservationId = reservation.getId().toString();
    String sportName = reservation.getSport().name().equals("FUTBOL") ? "Futbol" : "Padel";
    String description = reservation.getCourt().getName()
        + " - "
        + reservation.getReservationDate()
        + " "
        + reservation.getReservationTime().toString().substring(0, 5);

    SessionCreateParams params = SessionCreateParams.builder()
        .setMode(SessionCreateParams.Mode.PAYMENT)
        .setSuccessUrl(successUrl)
        .setCancelUrl(cancelUrl)
        .setClientReferenceId(reservationId)
        .setCustomerEmail(client.getEmail())
        .putMetadata("reservationId", reservationId)
        .putMetadata("clientId", client.getId().toString())
        .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
        .addLineItem(
            SessionCreateParams.LineItem.builder()
                .setQuantity(1L)
                .setPriceData(
                    SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency("eur")
                        .setUnitAmount(amountInCents)
                        .setProductData(
                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName("Reserva de " + sportName)
                                .setDescription(description)
                                .build()
                        )
                        .build()
                )
                .build()
        )
        .build();

    Session session = Session.create(params, requestOptions);
    return new StripeCheckoutSession(session.getId(), session.getUrl());
  }

  @Override
  public void expireSession(String stripeSessionId) throws StripeException {
    Session session = Session.retrieve(stripeSessionId, requestOptions());
    session.expire(requestOptions());
  }

  private RequestOptions requestOptions() {
    if (secretKey == null || secretKey.isBlank()) {
      throw new IllegalStateException("STRIPE_SECRET_KEY is not configured");
    }
    return RequestOptions.builder().setApiKey(secretKey).build();
  }
}
