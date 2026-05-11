package com.alvar.oasisclub.reservations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.alvar.oasisclub.courts.entity.CourtEntity;
import com.alvar.oasisclub.courts.repository.CourtRepository;
import com.alvar.oasisclub.clients.service.ClientService;
import com.alvar.oasisclub.common.email.EmailService;
import com.alvar.oasisclub.reservations.dto.AvailabilitySlotResponse;
import com.alvar.oasisclub.reservations.dto.CreateReservationRequest;
import com.alvar.oasisclub.reservations.dto.ReservationResponse;
import com.alvar.oasisclub.reservations.entity.ReservationEntity;
import com.alvar.oasisclub.reservations.entity.ReservationStatus;
import com.alvar.oasisclub.reservations.entity.SportType;
import com.alvar.oasisclub.reservations.mapper.ReservationMapper;
import com.alvar.oasisclub.reservations.repository.ReservationRepository;
import com.alvar.oasisclub.reservations.service.ReservationService;
import com.alvar.oasisclub.schedule.service.ScheduleSlotService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

  @Mock
  private ReservationRepository reservationRepository;

  @Mock
  private ReservationMapper reservationMapper;

  @Mock
  private CourtRepository courtRepository;

  @Mock
  private ScheduleSlotService scheduleSlotService;

  @Mock
  private ClientService clientService;

  @Mock
  private EmailService emailService;

  @InjectMocks
  private ReservationService reservationService;

  private CourtEntity court(UUID id, boolean active) {
    CourtEntity court = new CourtEntity();
    court.setId(id);
    court.setName("Pista 1");
    court.setSport(SportType.PADEL);
    court.setIsActive(active);
    return court;
  }

  @Test
  void createOk() {
    UUID courtId = UUID.randomUUID();

    CreateReservationRequest request = new CreateReservationRequest();
    request.setUserName("Ana");
    request.setSport(SportType.PADEL);
    request.setCourtId(courtId);
    request.setDate(LocalDate.of(2026, 3, 15));
    request.setTime(LocalTime.of(18, 0));

    CourtEntity court = court(courtId, true);
    ReservationEntity entity = ReservationEntity.builder()
        .id(UUID.randomUUID())
        .clientId(UUID.randomUUID())
        .userName("Ana")
        .sport(SportType.PADEL)
        .court(court)
        .reservationDate(LocalDate.of(2026, 3, 15))
        .reservationTime(LocalTime.of(18, 0))
        .status(ReservationStatus.PENDING)
        .createdAt(LocalDateTime.now())
        .build();

    ReservationResponse response = new ReservationResponse();
    response.setUserName("Ana");

    when(courtRepository.findById(courtId)).thenReturn(Optional.of(court));
    when(reservationMapper.fromCreateRequest(request, court)).thenReturn(entity);
    when(reservationRepository.saveAndFlush(entity)).thenReturn(entity);
    when(reservationMapper.toResponse(entity)).thenReturn(response);

    ReservationResponse result = reservationService.createReservation(request);

    assertEquals("Ana", result.getUserName());
  }

  @Test
  void createTakenSlotThrows() {
    UUID courtId = UUID.randomUUID();
    LocalDate date = LocalDate.of(2026, 3, 15);
    LocalTime time = LocalTime.of(18, 0);

    CreateReservationRequest request = new CreateReservationRequest();
    request.setUserName("Ana");
    request.setSport(SportType.PADEL);
    request.setCourtId(courtId);
    request.setDate(date);
    request.setTime(time);

    when(courtRepository.findById(courtId)).thenReturn(Optional.of(court(courtId, true)));
    when(reservationRepository.existsByCourt_IdAndReservationDateAndReservationTime(courtId, date, time)).thenReturn(true);

    assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(request));
  }

  @Test
  void availabilityMarksTakenSlot() {
    UUID courtId = UUID.randomUUID();
    LocalDate date = LocalDate.of(2026, 3, 10);
    CourtEntity court = court(courtId, true);

    ReservationEntity occupied = ReservationEntity.builder()
        .id(UUID.randomUUID())
        .sport(SportType.PADEL)
        .court(court)
        .reservationDate(date)
        .reservationTime(LocalTime.of(18, 0))
        .status(ReservationStatus.PENDING)
        .createdAt(LocalDateTime.now())
        .build();

    ReservationEntity confirmed = ReservationEntity.builder()
        .id(UUID.randomUUID())
        .sport(SportType.PADEL)
        .court(court)
        .reservationDate(date)
        .reservationTime(LocalTime.of(19, 30))
        .status(ReservationStatus.CONFIRMED)
        .createdAt(LocalDateTime.now())
        .build();

    when(scheduleSlotService.getAllSlots()).thenReturn(List.of(
        "09:00",
        "10:30",
        "12:00",
        "13:30",
        "15:00",
        "16:30",
        "18:00",
        "19:30",
        "21:00"
    ));
    when(reservationRepository.findByCourt_IdAndReservationDateOrderByReservationTimeAsc(courtId, date))
        .thenReturn(List.of(occupied, confirmed));

    List<AvailabilitySlotResponse> slots = reservationService.getAvailability(courtId, date);

    assertEquals(9, slots.size());
    AvailabilitySlotResponse slot18 = slots.stream().filter(s -> s.getTime().equals("18:00")).findFirst().orElseThrow();
    AvailabilitySlotResponse slot1930 = slots.stream().filter(s -> s.getTime().equals("19:30")).findFirst().orElseThrow();
    AvailabilitySlotResponse slot09 = slots.stream().filter(s -> s.getTime().equals("09:00")).findFirst().orElseThrow();
    assertFalse(slot18.isAvailable());
    assertFalse(slot1930.isAvailable());
    assertTrue(slot09.isAvailable());
  }

  @Test
  void confirmByStripeSessionIsIdempotent() {
    ReservationEntity confirmed = ReservationEntity.builder()
        .id(UUID.randomUUID())
        .stripeSessionId("cs_test_123")
        .status(ReservationStatus.CONFIRMED)
        .createdAt(LocalDateTime.now())
        .build();

    when(reservationRepository.findByStripeSessionId("cs_test_123")).thenReturn(Optional.of(confirmed));

    reservationService.confirmByStripeSessionId("cs_test_123");

    assertEquals(ReservationStatus.CONFIRMED, confirmed.getStatus());
  }
}


