package com.alvar.oasisclub.payments.service;

import com.alvar.oasisclub.auth.security.AuthenticatedUser;
import com.alvar.oasisclub.clients.entity.ClientEntity;
import com.alvar.oasisclub.clients.service.ClientService;
import com.alvar.oasisclub.common.config.AppCorsProperties;
import com.alvar.oasisclub.common.config.AppFrontendUrlProperties;
import com.alvar.oasisclub.payments.dto.CancelCheckoutSessionRequest;
import com.alvar.oasisclub.payments.dto.CheckoutSessionResponse;
import com.alvar.oasisclub.payments.dto.CreateCheckoutSessionRequest;
import com.alvar.oasisclub.reservations.dto.CreateReservationRequest;
import com.alvar.oasisclub.reservations.entity.ReservationEntity;
import com.alvar.oasisclub.reservations.entity.ReservationStatus;
import com.alvar.oasisclub.reservations.entity.SportType;
import com.alvar.oasisclub.reservations.service.ReservationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PaymentService {

  private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
  private static final long FUTBOL_PRICE_CENTS = 10000L;
  private static final long PADEL_PRICE_CENTS = 5000L;

  private final ReservationService reservationService;
  private final ClientService clientService;
  private final StripeCheckoutClient stripeCheckoutClient;
  private final AppFrontendUrlProperties frontendUrlProperties;
  private final AppCorsProperties corsProperties;
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final String webhookSecret;

  public PaymentService(
      ReservationService reservationService,
      ClientService clientService,
      StripeCheckoutClient stripeCheckoutClient,
      AppFrontendUrlProperties frontendUrlProperties,
      AppCorsProperties corsProperties,
      @Value("${app.stripe.webhook-secret:}") String webhookSecret
  ) {
    this.reservationService = reservationService;
    this.clientService = clientService;
    this.stripeCheckoutClient = stripeCheckoutClient;
    this.frontendUrlProperties = frontendUrlProperties;
    this.corsProperties = corsProperties;
    this.webhookSecret = webhookSecret;
  }

  @Transactional
  public CheckoutSessionResponse createCheckoutSession(
      CreateCheckoutSessionRequest request,
      AuthenticatedUser user,
      String requestOrigin
  ) {
    requireMember(user);
    ClientEntity client = clientService.getEntityById(user.clientId());
    CreateReservationRequest reservationRequest = new CreateReservationRequest();
    reservationRequest.setClientId(client.getId().toString());
    reservationRequest.setUserName(client.getName());
    reservationRequest.setSport(request.getSport());
    reservationRequest.setCourtId(request.getCourtId());
    reservationRequest.setDate(request.getDate());
    reservationRequest.setTime(request.getTime());

    ReservationEntity reservation = reservationService.createPendingReservationForPayment(reservationRequest);
    long amountInCents = priceFor(request.getSport());
    String checkoutFrontendUrl = frontendUrl(requestOrigin);

    try {
      StripeCheckoutSession checkoutSession = stripeCheckoutClient.createReservationCheckoutSession(
          reservation,
          client,
          amountInCents,
          checkoutSuccessUrl(checkoutFrontendUrl),
          checkoutCancelUrl(checkoutFrontendUrl)
      );
      reservationService.saveStripeSessionId(reservation.getId(), checkoutSession.id());
      return new CheckoutSessionResponse(
          reservation.getId().toString(),
          checkoutSession.id(),
          checkoutSession.url()
      );
    } catch (StripeException ex) {
      log.warn("Stripe checkout creation failed: {}", stripeErrorMessage(ex));
      throw new IllegalStateException(paymentProviderErrorMessage(ex, "crear"), ex);
    }
  }

  @Transactional
  public void cancelCheckoutSession(CancelCheckoutSessionRequest request, AuthenticatedUser user) {
    ReservationEntity reservation = reservationService.getByStripeSessionIdAndClientId(
        request.getStripeSessionId(),
        user.clientId()
    );

    if (reservation.getStatus() != ReservationStatus.PENDING) {
      return;
    }

    try {
      stripeCheckoutClient.expireSession(request.getStripeSessionId());
      reservationService.releasePendingStripeReservation(request.getStripeSessionId());
    } catch (StripeException ex) {
      log.warn("Stripe checkout cancellation failed: {}", stripeErrorMessage(ex));
      throw new IllegalStateException(paymentProviderErrorMessage(ex, "cancelar"), ex);
    }
  }

  public void handleWebhook(String payload, String signatureHeader) {
    Event event = verifyEvent(payload, signatureHeader);
    JsonNode sessionNode = readSessionNode(payload);
    String stripeSessionId = sessionNode.path("id").asText();

    if (stripeSessionId == null || stripeSessionId.isBlank()) {
      return;
    }

    if ("checkout.session.completed".equals(event.getType())) {
      String paymentStatus = sessionNode.path("payment_status").asText();
      if ("paid".equals(paymentStatus)) {
        reservationService.confirmByStripeSessionId(stripeSessionId);
      }
      return;
    }

    if ("checkout.session.expired".equals(event.getType())) {
      reservationService.releasePendingStripeReservation(stripeSessionId);
    }
  }

  private Event verifyEvent(String payload, String signatureHeader) {
    if (webhookSecret == null || webhookSecret.isBlank()) {
      throw new IllegalStateException("STRIPE_WEBHOOK_SECRET is not configured");
    }
    try {
      return Webhook.constructEvent(payload, signatureHeader, webhookSecret);
    } catch (SignatureVerificationException ex) {
      throw new AccessDeniedException("Invalid Stripe webhook signature", ex);
    }
  }

  private JsonNode readSessionNode(String payload) {
    try {
      return objectMapper.readTree(payload).path("data").path("object");
    } catch (Exception ex) {
      throw new IllegalArgumentException("Invalid Stripe webhook payload");
    }
  }

  private long priceFor(SportType sport) {
    return switch (sport) {
      case FUTBOL -> FUTBOL_PRICE_CENTS;
      case PADEL -> PADEL_PRICE_CENTS;
    };
  }

  private void requireMember(AuthenticatedUser user) {
    if (!"MEMBER".equals(user.role())) {
      throw new AccessDeniedException("Solo los socios pueden pagar reservas desde checkout");
    }
  }

  private String checkoutSuccessUrl(String checkoutFrontendUrl) {
    return checkoutFrontendUrl + "/pago-completado?session_id={CHECKOUT_SESSION_ID}";
  }

  private String checkoutCancelUrl(String checkoutFrontendUrl) {
    return checkoutFrontendUrl + "/pago-cancelado?session_id={CHECKOUT_SESSION_ID}";
  }

  private String frontendUrl(String requestOrigin) {
    String frontendUrl = corsProperties.isAllowedOrigin(requestOrigin)
        ? requestOrigin
        : frontendUrlProperties.getFrontendUrl();
    if (frontendUrl.endsWith("/")) {
      return frontendUrl.substring(0, frontendUrl.length() - 1);
    }
    return frontendUrl;
  }

  private String stripeErrorMessage(StripeException ex) {
    if (ex.getStripeError() == null) {
      return ex.getMessage();
    }
    return ex.getStripeError().getType() + " - " + ex.getStripeError().getMessage();
  }

  private String paymentProviderErrorMessage(StripeException ex, String action) {
    if (ex.getStatusCode() == 401) {
      return "Stripe ha rechazado STRIPE_SECRET_KEY. Copia una Secret key sk_test valida desde Stripe.";
    }
    return "No se pudo " + action + " la sesion de pago de Stripe. Revisa la configuracion de Stripe.";
  }
}
