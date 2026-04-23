package com.alvar.oasisclub.clients.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class ClientResponse {
  private String id;
  private String name;
  private String email;
  private String role;
  private LocalDate joinDate;
  private String phone;
  private LocalDate birthDate;
}
