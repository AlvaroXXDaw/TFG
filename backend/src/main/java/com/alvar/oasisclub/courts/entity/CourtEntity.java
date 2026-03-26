package com.alvar.oasisclub.courts.entity;

import com.alvar.oasisclub.reservations.entity.SportType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "courts")
@Getter
@Setter
@NoArgsConstructor
public class CourtEntity {

  @Id
  private UUID id;

  @Column(nullable = false, length = 120)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private SportType sport;

  @Column(nullable = false)
  private Boolean isActive = true;

  @Column(nullable = false)
  private LocalDateTime createdAt = LocalDateTime.now();
  
}

