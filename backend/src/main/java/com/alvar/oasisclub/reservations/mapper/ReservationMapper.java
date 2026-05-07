package com.alvar.oasisclub.reservations.mapper;

import com.alvar.oasisclub.courts.entity.CourtEntity;
import com.alvar.oasisclub.reservations.dto.CreateMaintenanceBlockRequest;
import com.alvar.oasisclub.reservations.dto.CreateReservationRequest;
import com.alvar.oasisclub.reservations.dto.ReservationResponse;
import com.alvar.oasisclub.reservations.entity.ReservationEntity;
import com.alvar.oasisclub.reservations.entity.ReservationStatus;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

  public ReservationResponse toResponse(ReservationEntity entity) {
    ReservationResponse dto = new ReservationResponse();
    dto.setId(entity.getId().toString());
    dto.setClientId(entity.getClientId() == null ? null : entity.getClientId().toString());
    dto.setUserName(entity.getUserName());
    dto.setSport(entity.getSport().name());
    dto.setCourt(entity.getCourt().getName());
    dto.setDate(entity.getReservationDate());
    dto.setTime(entity.getReservationTime());
    dto.setStatus(entity.getStatus().name());
    dto.setStripeSessionId(entity.getStripeSessionId());
    return dto;
  }

  public ReservationEntity fromCreateRequest(CreateReservationRequest request, CourtEntity court) {
    UUID clientId = null;
    if (request.getClientId() != null && !request.getClientId().isBlank()) {
      clientId = UUID.fromString(request.getClientId());
    }

    return ReservationEntity.builder()
        .clientId(clientId)
        .userName(request.getUserName().trim())
        .sport(request.getSport())
        .court(court)
        .reservationDate(request.getDate())
        .reservationTime(request.getTime())
        .status(ReservationStatus.PENDING)
        .createdAt(LocalDateTime.now())
        .build();
  }

  public ReservationEntity fromMaintenanceRequest(CreateMaintenanceBlockRequest request, CourtEntity court) {
    return ReservationEntity.builder()
        .clientId(null)
        .userName("Mantenimiento")
        .sport(request.getSport())
        .court(court)
        .reservationDate(request.getDate())
        .reservationTime(request.getTime())
        .status(ReservationStatus.MAINTENANCE)
        .createdAt(LocalDateTime.now())
        .build();
  }
}

