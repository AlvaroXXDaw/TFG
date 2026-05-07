package com.alvar.oasisclub.reservations.controller;

import com.alvar.oasisclub.auth.security.AccessControlService;
import com.alvar.oasisclub.auth.security.AuthenticatedUser;
import com.alvar.oasisclub.clients.entity.ClientEntity;
import com.alvar.oasisclub.clients.service.ClientService;
import com.alvar.oasisclub.reservations.dto.AvailabilitySlotResponse;
import com.alvar.oasisclub.reservations.dto.CreateMaintenanceBlockRequest;
import com.alvar.oasisclub.reservations.dto.CreateReservationRequest;
import com.alvar.oasisclub.reservations.dto.ReservationResponse;
import com.alvar.oasisclub.reservations.entity.ReservationEntity;
import com.alvar.oasisclub.reservations.entity.SportType;
import com.alvar.oasisclub.reservations.service.ReservationService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class ReservationController {

  private final ReservationService reservationService;
  private final AccessControlService accessControl;
  private final ClientService clientService;

  @GetMapping("/reservations")
  public List<ReservationResponse> getReservations(
      @RequestParam(required = false) String sport,
      @RequestParam(required = false) String status,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
      Authentication authentication
  ) {
    if (!accessControl.isAdmin(authentication)) {
      AuthenticatedUser user = accessControl.requireUser(authentication);
      return reservationService.getReservationsByClient(user.clientId(), sport, status, date);
    }

    return reservationService.getReservations(sport, status, date);
  }

  @PostMapping("/reservations")
  public ResponseEntity<ReservationResponse> createReservation(
      @Valid @RequestBody CreateReservationRequest request,
      Authentication authentication
  ) {
    accessControl.requireUser(authentication);
    boolean isAdmin = accessControl.isAdmin(authentication);

    if (!isAdmin) {
      throw new org.springframework.security.access.AccessDeniedException(
          "Users must pay reservations through checkout"
      );
    }

    UUID effectiveClientId;
    if (request.getClientId() == null || request.getClientId().isBlank()) {
      throw new IllegalArgumentException("clientId is required for admin reservation creation");
    }
    effectiveClientId = UUID.fromString(request.getClientId());

    ClientEntity targetClient = clientService.getEntityById(effectiveClientId);

    request.setClientId(targetClient.getId().toString());
    request.setUserName(targetClient.getName());

    return ResponseEntity.ok(reservationService.createConfirmedReservation(request));
  }

  @PostMapping("/reservations/maintenance")
  public ResponseEntity<ReservationResponse> createMaintenance(
      @Valid @RequestBody CreateMaintenanceBlockRequest request,
      Authentication authentication
  ) {
    accessControl.requireAdmin(authentication);
    return ResponseEntity.ok(reservationService.createMaintenanceBlock(request));
  }

  @DeleteMapping("/reservations/{id}")
  public ResponseEntity<Void> deleteReservation(@PathVariable UUID id, Authentication authentication) {
    if (!accessControl.isAdmin(authentication)) {
      AuthenticatedUser user = accessControl.requireUser(authentication);
      ReservationEntity reservation = reservationService.getEntityById(id);
      if (reservation.getClientId() == null || !reservation.getClientId().equals(user.clientId())) {
        throw new org.springframework.security.access.AccessDeniedException("Not allowed to delete this reservation");
      }
    }

    reservationService.deleteReservation(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/availability")
  public List<AvailabilitySlotResponse> getAvailability(
      @RequestParam UUID courtId,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
  ) {
    return reservationService.getAvailability(courtId, date);
  }
}

