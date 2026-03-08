package com.alvar.oasisclub.auth.controller;

import com.alvar.oasisclub.auth.dto.AuthSessionResponse;
import com.alvar.oasisclub.auth.dto.ForgotPasswordRequest;
import com.alvar.oasisclub.auth.dto.LoginRequest;
import com.alvar.oasisclub.auth.dto.RegisterRequest;
import com.alvar.oasisclub.auth.dto.ResetPasswordRequest;
import com.alvar.oasisclub.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<AuthSessionResponse> login(@Valid @RequestBody LoginRequest request) {
    return ResponseEntity.ok(authService.login(request));
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
    authService.forgotPassword(request.getEmail());
    return ResponseEntity.ok().build();
  }

  @PostMapping("/reset-password")
  public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
    authService.resetPassword(request.getToken(), request.getNewPassword());
    return ResponseEntity.ok().build();
  }

  @PostMapping("/register")
  public ResponseEntity<AuthSessionResponse> register(@Valid @RequestBody RegisterRequest request) {
    return ResponseEntity.ok(authService.register(request));
  }
}

