package com.alvar.oasisclub.clients.dto;

import com.alvar.oasisclub.clients.entity.ClientPlan;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateClientPlanRequest {

  @NotNull
  private ClientPlan plan;
}

