package com.alvar.oasisclub.schedule.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "schedule_slots")
@Getter
@Setter
@NoArgsConstructor
public class ScheduleSlotEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "slot_time", nullable = false, unique = true)
  private LocalTime slotTime;

  public ScheduleSlotEntity(LocalTime slotTime) {
    this.slotTime = slotTime;
  }
}
