package com.alvar.oasisclub.reservations.repository;

import com.alvar.oasisclub.reservations.entity.ReservationEntity;
import com.alvar.oasisclub.reservations.entity.ReservationStatus;
import com.alvar.oasisclub.reservations.entity.SportType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<ReservationEntity, UUID> {

  List<ReservationEntity> findAllByOrderByReservationDateDescReservationTimeDesc();

  List<ReservationEntity> findBySportOrderByReservationDateDescReservationTimeDesc(SportType sport);

  List<ReservationEntity> findByStatusOrderByReservationDateDescReservationTimeDesc(ReservationStatus status);

  List<ReservationEntity> findByReservationDateOrderByReservationTimeAsc(LocalDate date);

  List<ReservationEntity> findBySportAndStatusAndReservationDateOrderByReservationTimeAsc(
      SportType sport,
      ReservationStatus status,
      LocalDate date
  );

  List<ReservationEntity> findByCourt_IdAndReservationDateOrderByReservationTimeAsc(UUID courtId, LocalDate date);

  List<ReservationEntity> findByClientIdOrderByReservationDateDescReservationTimeDesc(UUID clientId);

  List<ReservationEntity> findByClientIdAndReservationDateOrderByReservationTimeAsc(UUID clientId, LocalDate date);

  Optional<ReservationEntity> findByStripeSessionId(String stripeSessionId);

  Optional<ReservationEntity> findByStripeSessionIdAndClientId(String stripeSessionId, UUID clientId);

  boolean existsByCourt_IdAndReservationDateAndReservationTime(UUID courtId, LocalDate date, LocalTime time);
}


