package com.alvar.oasisclub.payments.controller;

import com.alvar.oasisclub.auth.security.AccessControlService;
import com.alvar.oasisclub.auth.security.AuthenticatedUser;
import com.alvar.oasisclub.payments.dto.CancelCheckoutSessionRequest;
import com.alvar.oasisclub.payments.dto.CheckoutSessionResponse;
import com.alvar.oasisclub.payments.dto.CreateCheckoutSessionRequest;
import com.alvar.oasisclub.payments.service.PaymentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
@AllArgsConstructor
public class PaymentController {

  private final PaymentService paymentService;
  private final AccessControlService accessControl;

  @PostMapping("/create-checkout-session")
  public ResponseEntity<CheckoutSessionResponse> createCheckoutSession(
      @Valid @RequestBody CreateCheckoutSessionRequest request,
      @RequestHeader(value = "Origin", required = false) String requestOrigin,
      Authentication authentication
  ) {
    AuthenticatedUser user = accessControl.requireUser(authentication);
    return ResponseEntity.ok(paymentService.createCheckoutSession(request, user, requestOrigin));
  }

  @PostMapping("/cancel-checkout-session")
  public ResponseEntity<Void> cancelCheckoutSession(
      @Valid @RequestBody CancelCheckoutSessionRequest request,
      Authentication authentication
  ) {
    AuthenticatedUser user = accessControl.requireUser(authentication);
    paymentService.cancelCheckoutSession(request, user);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/webhook")
  public ResponseEntity<Void> webhook(
      @RequestBody String payload,
      @RequestHeader("Stripe-Signature") String signatureHeader
  ) {
    paymentService.handleWebhook(payload, signatureHeader);
    return ResponseEntity.ok().build();
  }
}
