package com.alvar.oasisclub.schedule.service;

import com.alvar.oasisclub.reservations.entity.ReservationStatus;
import com.alvar.oasisclub.reservations.repository.ReservationRepository;
import com.alvar.oasisclub.schedule.entity.ScheduleSlotEntity;
import com.alvar.oasisclub.schedule.repository.ScheduleSlotRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ScheduleSlotService {

  private final ScheduleSlotRepository scheduleSlotRepository;
  private final ReservationRepository reservationRepository;

  @Transactional(readOnly = true)
  public List<String> getAllSlots() {
    return scheduleSlotRepository.findAllByOrderBySlotTimeAsc().stream()
        .map(slot -> slot.getSlotTime().toString().substring(0, 5))
        .toList();
  }

  @Transactional
  public String addSlot(String time) {
    LocalTime parsed = LocalTime.parse(time);
    if (scheduleSlotRepository.existsBySlotTime(parsed)) {
      throw new IllegalArgumentException("El horario " + time + " ya existe");
    }
    ScheduleSlotEntity saved = scheduleSlotRepository.save(new ScheduleSlotEntity(parsed));
    return saved.getSlotTime().toString().substring(0, 5);
  }

  @Transactional
  public void removeSlot(String time) {
    LocalTime parsed = LocalTime.parse(time);
    if (!scheduleSlotRepository.existsBySlotTime(parsed)) {
      throw new IllegalArgumentException("El horario " + time + " no existe");
    }

    // Bloquear eliminación si hay reservas futuras pendientes en ese horario
    boolean hasFutureReservations = reservationRepository
        .findAll().stream()
        .anyMatch(r ->
            r.getReservationTime().equals(parsed)
            && r.getReservationDate().isAfter(LocalDate.now().minusDays(1))
            && r.getStatus() == ReservationStatus.PENDING
        );

    if (hasFutureReservations) {
      throw new IllegalArgumentException(
          "No se puede eliminar el horario " + time + " porque tiene reservas pendientes"
      );
    }

    scheduleSlotRepository.deleteBySlotTime(parsed);
  }
}
