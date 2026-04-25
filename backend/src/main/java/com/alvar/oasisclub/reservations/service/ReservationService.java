package com.alvar.oasisclub.reservations.service;

import com.alvar.oasisclub.courts.repository.CourtRepository;
import com.alvar.oasisclub.reservations.dto.AvailabilitySlotResponse;
import com.alvar.oasisclub.reservations.dto.CreateMaintenanceBlockRequest;
import com.alvar.oasisclub.reservations.dto.CreateReservationRequest;
import com.alvar.oasisclub.reservations.dto.ReservationResponse;
import com.alvar.oasisclub.reservations.entity.ReservationEntity;
import com.alvar.oasisclub.reservations.entity.ReservationStatus;
import com.alvar.oasisclub.reservations.entity.SportType;
import com.alvar.oasisclub.reservations.exception.ReservationNotFoundException;
import com.alvar.oasisclub.reservations.mapper.ReservationMapper;
import com.alvar.oasisclub.reservations.repository.ReservationRepository;
import com.alvar.oasisclub.schedule.service.ScheduleSlotService;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ReservationService {

  private final ReservationRepository reservationRepository;
  private final ReservationMapper reservationMapper;
  private final CourtRepository courtRepository;
  private final ScheduleSlotService scheduleSlotService;

  @Transactional(readOnly = true)
  public List<ReservationResponse> getReservations(String sport, String status, LocalDate date) {
    SportType parsedSport = sport == null ? null : SportType.valueOf(sport.toUpperCase());
    ReservationStatus parsedStatus = status == null ? null : ReservationStatus.valueOf(status.toUpperCase());

    List<ReservationEntity> entities;
    if (date != null) {
      entities = reservationRepository.findByReservationDateOrderByReservationTimeAsc(date);
    } else {
      entities = reservationRepository.findAllByOrderByReservationDateDescReservationTimeDesc();
    }

    return entities.stream()
        .filter(entity -> parsedSport == null || entity.getSport() == parsedSport)
        .filter(entity -> parsedStatus == null || entity.getStatus() == parsedStatus)
        .map(reservationMapper::toResponse)
        .toList();
  }

  @Transactional(readOnly = true)
  public List<ReservationEntity> getReservationsByClient(UUID clientId) {
    return reservationRepository.findByClientIdOrderByReservationDateDescReservationTimeDesc(clientId);
  }

  @Transactional(readOnly = true)
  public List<ReservationResponse> getReservationsByClient(
      UUID clientId,
      String sport,
      String status,
      LocalDate date
  ) {
    SportType parsedSport = sport == null ? null : SportType.valueOf(sport.toUpperCase());
    ReservationStatus parsedStatus = status == null ? null : ReservationStatus.valueOf(status.toUpperCase());

    return reservationRepository.findByClientIdOrderByReservationDateDescReservationTimeDesc(clientId).stream()
        .filter(entity -> parsedSport == null || entity.getSport() == parsedSport)
        .filter(entity -> parsedStatus == null || entity.getStatus() == parsedStatus)
        .filter(entity -> date == null || entity.getReservationDate().equals(date))
        .map(reservationMapper::toResponse)
        .toList();
  }

  @Transactional(readOnly = true)
  public ReservationEntity getEntityById(UUID id) {
    return reservationRepository.findById(id)
        .orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));
  }

  @Transactional
  public ReservationResponse createReservation(CreateReservationRequest request) {
    com.alvar.oasisclub.courts.entity.CourtEntity court = requireActiveCourt(request.getCourtId());
    if (court.getSport() != request.getSport()) {
      throw new IllegalArgumentException("Court sport does not match reservation sport");
    }

    ensureSlotAvailable(court.getId(), request.getDate(), request.getTime());

    try {
      ReservationEntity saved = reservationRepository.save(reservationMapper.fromCreateRequest(request, court));
      return reservationMapper.toResponse(saved);
    } catch (DataIntegrityViolationException ex) {
      throw new IllegalArgumentException("Selected slot is not available");
    }
  }

  @Transactional(readOnly = true)
  public long countActiveClientReservationsByDay(UUID clientId, LocalDate date) {
    return reservationRepository.findByClientIdAndReservationDateOrderByReservationTimeAsc(clientId, date).stream()
        .filter(reservation -> reservation.getStatus() == ReservationStatus.PENDING)
        .count();
  }

  @Transactional
  public ReservationResponse createMaintenanceBlock(CreateMaintenanceBlockRequest request) {
    com.alvar.oasisclub.courts.entity.CourtEntity court = requireActiveCourt(request.getCourtId());
    if (court.getSport() != request.getSport()) {
      throw new IllegalArgumentException("Court sport does not match maintenance sport");
    }

    ensureSlotAvailable(court.getId(), request.getDate(), request.getTime());

    try {
      ReservationEntity saved = reservationRepository.save(reservationMapper.fromMaintenanceRequest(request, court));
      return reservationMapper.toResponse(saved);
    } catch (DataIntegrityViolationException ex) {
      throw new IllegalArgumentException("Selected slot is not available");
    }
  }

  @Transactional
  public void deleteReservation(UUID id) {
    if (!reservationRepository.existsById(id)) {
      throw new ReservationNotFoundException("Reservation not found");
    }
    reservationRepository.deleteById(id);
  }

  @Transactional(readOnly = true)
  public List<AvailabilitySlotResponse> getAvailability(UUID courtId, LocalDate date) {
    List<String> times = scheduleSlotService.getAllSlots();
    List<ReservationEntity> dayReservations = reservationRepository
        .findByCourt_IdAndReservationDateOrderByReservationTimeAsc(courtId, date);

    List<String> reservedTimes = dayReservations.stream()
        .filter(r -> r.getStatus() != ReservationStatus.COMPLETED)
        .map(r -> r.getReservationTime().toString().substring(0, 5))
        .toList();

    List<AvailabilitySlotResponse> result = new ArrayList<>();
    for (String time : times) {
      result.add(new AvailabilitySlotResponse(time, !reservedTimes.contains(time)));
    }
    return result;
  }

  private com.alvar.oasisclub.courts.entity.CourtEntity requireActiveCourt(UUID courtId) {
    com.alvar.oasisclub.courts.entity.CourtEntity court = courtRepository.findById(courtId)
        .orElseThrow(() -> new IllegalArgumentException("Court not found"));
    if (Boolean.FALSE.equals(court.getIsActive())) {
      throw new IllegalArgumentException("Court is inactive");
    }
    return court;
  }

  private void ensureSlotAvailable(UUID courtId, LocalDate date, LocalTime time) {
    if (reservationRepository.existsByCourt_IdAndReservationDateAndReservationTime(courtId, date, time)) {
      throw new IllegalArgumentException("Selected slot is not available");
    }
  }
}

