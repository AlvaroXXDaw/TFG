package com.alvar.oasisclub.clients.dto;

import com.alvar.oasisclub.clients.entity.ClientPlan;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;

@Data
public class CreateClientRequest {

  @NotBlank
  private String name;

  @Email
  @NotBlank
  private String email;

  @NotNull
  private ClientPlan plan;

  @NotBlank
  private String password;

  @NotBlank
  private String phone;

  @NotNull
  private LocalDate birthDate;
}


