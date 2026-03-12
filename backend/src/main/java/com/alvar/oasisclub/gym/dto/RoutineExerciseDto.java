package com.alvar.oasisclub.gym.dto;

import lombok.Data;

@Data
public class RoutineExerciseDto {
  private String id;
  private Integer order;
  private String name;
  private Integer sets;
  private String reps;
  private String rest;
}


