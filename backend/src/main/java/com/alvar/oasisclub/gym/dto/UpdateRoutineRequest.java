package com.alvar.oasisclub.gym.dto;

import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class UpdateRoutineRequest {

  @NotNull
  private List<RoutineDayDto> days = new ArrayList<>();
}


