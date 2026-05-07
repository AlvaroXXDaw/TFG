package com.alvar.oasisclub.payments;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.alvar.oasisclub.auth.security.AuthenticatedUser;
import com.alvar.oasisclub.clients.entity.ClientEntity;
import com.alvar.oasisclub.clients.service.ClientService;
import com.alvar.oasisclub.common.config.AppCorsProperties;
import com.alvar.oasisclub.common.config.AppFrontendUrlProperties;
import com.alvar.oasisclub.payments.dto.CancelCheckoutSessionRequest;
import com.alvar.oasisclub.payments.dto.CheckoutSessionResponse;
import com.alvar.oasisclub.payments.dto.CreateCheckoutSessionRequest;
import com.alvar.oasisclub.payments.service.PaymentService;
import com.alvar.oasisclub.payments.service.StripeCheckoutClient;
import com.alvar.oasisclub.payments.service.StripeCheckoutSession;
import com.alvar.oasisclub.reservations.dto.CreateReservationRequest;
import com.alvar.oasisclub.reservations.entity.ReservationEntity;
import com.alvar.oasisclub.reservations.entity.ReservationStatus;
import com.alvar.oasisclub.reservations.entity.SportType;
import com.alvar.oasisclub.reservations.exception.ReservationNotFoundException;
import com.alvar.oasisclub.reservations.service.ReservationService;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HexFormat;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

  private static final String WEBHOOK_SECRET = "whsec_test_secret";

  @Mock
  private ReservationService reservationService;

  @Mock
  private ClientService clientService;

  @Mock
  private StripeCheckoutClient stripeCheckoutClient;

  @Mock
  private AppFrontendUrlProperties frontendUrlProperties;

  private AppCorsProperties corsProperties;

  private PaymentService paymentService;

  @BeforeEach
  void setUp() {
    corsProperties = new AppCorsProperties();
    ReflectionTestUtils.setField(corsProperties, "allowedOrigins", java.util.List.of("http://localhost:4300"));
    ReflectionTestUtils.setField(corsProperties, "allowedOriginPatterns", java.util.List.of("http://localhost:*"));
    paymentService = new PaymentService(
        reservationService,
        clientService,
        stripeCheckoutClient,
        frontendUrlProperties,
        corsProperties,
        WEBHOOK_SECRET
    );
  }

  @Test
  void createCheckoutSessionCreatesPendingReservationAndStoresStripeSession() throws Exception {
    UUID clientId = UUID.randomUUID();
    UUID reservationId = UUID.randomUUID();
    UUID courtId = UUID.randomUUID();
    AuthenticatedUser user = new AuthenticatedUser(clientId, "ana@example.com", "MEMBER");

    ClientEntity client = ClientEntity.builder()
        .id(clientId)
        .name("Ana")
        .email("ana@example.com")
        .build();
    ReservationEntity reservation = ReservationEntity.builder()
        .id(reservationId)
        .clientId(clientId)
        .userName("Ana")
        .sport(SportType.PADEL)
        .reservationDate(LocalDate.of(2026, 3, 15))
        .reservationTime(LocalTime.of(18, 0))
        .status(ReservationStatus.PENDING)
        .createdAt(LocalDateTime.now())
        .build();

    CreateCheckoutSessionRequest request = new CreateCheckoutSessionRequest();
    request.setSport(SportType.PADEL);
    request.setCourtId(courtId);
    request.setDate(LocalDate.of(2026, 3, 15));
    request.setTime(LocalTime.of(18, 0));

    when(clientService.getEntityById(clientId)).thenReturn(client);
    when(reservationService.createPendingReservationForPayment(any(CreateReservationRequest.class)))
        .thenReturn(reservation);
    when(stripeCheckoutClient.createReservationCheckoutSession(
        eq(reservation),
        eq(client),
        eq(5000L),
        eq("http://localhost:4300/pago-completado?session_id={CHECKOUT_SESSION_ID}"),
        eq("http://localhost:4300/pago-cancelado?session_id={CHECKOUT_SESSION_ID}")
    )).thenReturn(new StripeCheckoutSession("cs_test_123", "https://checkout.stripe.test/session"));

    CheckoutSessionResponse response = paymentService.createCheckoutSession(request, user, "http://localhost:4300");

    assertEquals(reservationId.toString(), response.getReservationId());
    assertEquals("cs_test_123", response.getStripeSessionId());
    assertEquals("https://checkout.stripe.test/session", response.getCheckoutUrl());
    verify(reservationService).saveStripeSessionId(reservationId, "cs_test_123");

    ArgumentCaptor<CreateReservationRequest> captor = ArgumentCaptor.forClass(CreateReservationRequest.class);
    verify(reservationService).createPendingReservationForPayment(captor.capture());
    assertEquals(clientId.toString(), captor.getValue().getClientId());
    assertEquals("Ana", captor.getValue().getUserName());
    assertEquals(SportType.PADEL, captor.getValue().getSport());
  }

  @Test
  void adminCannotCreateCheckoutSessionAsReservationOwner() throws Exception {
    AuthenticatedUser admin = new AuthenticatedUser(UUID.randomUUID(), "admin@example.com", "ADMIN");
    CreateCheckoutSessionRequest request = new CreateCheckoutSessionRequest();
    request.setSport(SportType.PADEL);
    request.setCourtId(UUID.randomUUID());
    request.setDate(LocalDate.of(2026, 3, 15));
    request.setTime(LocalTime.of(18, 0));

    assertThrows(
        AccessDeniedException.class,
        () -> paymentService.createCheckoutSession(request, admin, "http://localhost:4300")
    );

    verify(clientService, never()).getEntityById(any(UUID.class));
    verify(reservationService, never()).createPendingReservationForPayment(any(CreateReservationRequest.class));
    verify(stripeCheckoutClient, never()).createReservationCheckoutSession(
        any(),
        any(),
        anyLong(),
        anyString(),
        anyString()
    );
  }

  @Test
  void signedCompletedWebhookConfirmsPaidReservation() throws Exception {
    String payload = """
        {"id":"evt_test","object":"event","type":"checkout.session.completed","data":{"object":{"id":"cs_test_123","object":"checkout.session","payment_status":"paid"}}}
        """.trim();

    paymentService.handleWebhook(payload, signatureHeader(payload));

    verify(reservationService).confirmByStripeSessionId("cs_test_123");
  }

  @Test
  void repeatedCompletedWebhookStaysSafe() throws Exception {
    String payload = """
        {"id":"evt_test","object":"event","type":"checkout.session.completed","data":{"object":{"id":"cs_test_123","object":"checkout.session","payment_status":"paid"}}}
        """.trim();
    String signature = signatureHeader(payload);

    paymentService.handleWebhook(payload, signature);
    paymentService.handleWebhook(payload, signature);

    verify(reservationService, org.mockito.Mockito.times(2)).confirmByStripeSessionId("cs_test_123");
  }

  @Test
  void cancelCheckoutSessionReleasesOnlyPendingReservationFromSameUser() throws Exception {
    UUID clientId = UUID.randomUUID();
    AuthenticatedUser user = new AuthenticatedUser(clientId, "ana@example.com", "MEMBER");
    ReservationEntity reservation = ReservationEntity.builder()
        .id(UUID.randomUUID())
        .clientId(clientId)
        .stripeSessionId("cs_test_123")
        .status(ReservationStatus.PENDING)
        .build();
    CancelCheckoutSessionRequest request = new CancelCheckoutSessionRequest();
    request.setStripeSessionId("cs_test_123");

    when(reservationService.getByStripeSessionIdAndClientId("cs_test_123", clientId)).thenReturn(reservation);

    paymentService.cancelCheckoutSession(request, user);

    verify(stripeCheckoutClient).expireSession("cs_test_123");
    verify(reservationService).releasePendingStripeReservation("cs_test_123");
  }

  @Test
  void cancelCheckoutSessionDoesNotReleaseAnotherUserReservation() throws Exception {
    UUID clientId = UUID.randomUUID();
    AuthenticatedUser user = new AuthenticatedUser(clientId, "ana@example.com", "MEMBER");
    CancelCheckoutSessionRequest request = new CancelCheckoutSessionRequest();
    request.setStripeSessionId("cs_test_123");

    when(reservationService.getByStripeSessionIdAndClientId("cs_test_123", clientId))
        .thenThrow(new ReservationNotFoundException("Reservation not found"));

    assertThrows(ReservationNotFoundException.class, () -> paymentService.cancelCheckoutSession(request, user));

    verify(stripeCheckoutClient, never()).expireSession(anyString());
    verify(reservationService, never()).releasePendingStripeReservation(anyString());
  }

  private String signatureHeader(String payload) throws Exception {
    long timestamp = System.currentTimeMillis() / 1000;
    String signedPayload = timestamp + "." + payload;
    Mac mac = Mac.getInstance("HmacSHA256");
    mac.init(new SecretKeySpec(WEBHOOK_SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
    String signature = HexFormat.of().formatHex(mac.doFinal(signedPayload.getBytes(StandardCharsets.UTF_8)));
    return "t=" + timestamp + ",v1=" + signature;
  }
}
