package com.alvar.oasisclub.auth.dto;

import lombok.Data;

@Data
public class AuthSessionResponse {
  private String token;
  private String clientId;
  private String name;
  private String email;
  private String role;
}
