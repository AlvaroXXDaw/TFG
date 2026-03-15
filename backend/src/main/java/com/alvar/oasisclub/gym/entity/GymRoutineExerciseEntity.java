package com.alvar.oasisclub.gym.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "gym_routine_exercises")
public class GymRoutineExerciseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "routine_day_id", nullable = false)
  private UUID routineDayId;

  @Column(name = "exercise_order", nullable = false)
  private Integer exerciseOrder;

  @Column(nullable = false, length = 160)
  private String name;

  @Column(name = "sets_count", nullable = false)
  private Integer setsCount;

  @Column(nullable = false, length = 30)
  private String reps;

  @Column(name = "rest_interval", nullable = false, length = 30)
  private String restInterval;
}


