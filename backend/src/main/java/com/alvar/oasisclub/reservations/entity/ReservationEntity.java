package com.alvar.oasisclub.reservations.entity;

import com.alvar.oasisclub.courts.entity.CourtEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "reservations")
public class ReservationEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "client_id")
  private UUID clientId;

  @Column(name = "user_name", nullable = false, length = 120)
  private String userName;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private SportType sport;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "court_id", nullable = false)
  private CourtEntity court;

  @Column(name = "reservation_date", nullable = false)
  private LocalDate reservationDate;

  @Column(name = "reservation_time", nullable = false)
  private LocalTime reservationTime;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private ReservationStatus status;

  @Column(name = "stripe_session_id", length = 255, unique = true)
  private String stripeSessionId;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;
}


