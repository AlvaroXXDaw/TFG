package com.alvar.oasisclub.auth.service;

import com.alvar.oasisclub.auth.dto.AuthSessionResponse;
import com.alvar.oasisclub.auth.dto.LoginRequest;
import com.alvar.oasisclub.auth.dto.RegisterRequest;
import com.alvar.oasisclub.auth.entity.PasswordResetTokenEntity;
import com.alvar.oasisclub.auth.exception.EmailAlreadyRegisteredException;
import com.alvar.oasisclub.auth.exception.InvalidCredentialsException;
import com.alvar.oasisclub.auth.exception.PasswordResetTokenInvalidException;
import com.alvar.oasisclub.auth.mapper.AuthMapper;
import com.alvar.oasisclub.auth.repository.PasswordResetTokenRepository;
import com.alvar.oasisclub.auth.security.JwtService;
import com.alvar.oasisclub.clients.entity.ClientEntity;
import com.alvar.oasisclub.clients.service.ClientService;
import com.alvar.oasisclub.common.config.AppFrontendUrlProperties;
import com.alvar.oasisclub.common.email.EmailService;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class AuthService {

  private final ClientService clientService;
  private final AuthMapper authMapper;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;
  private final PasswordResetTokenRepository resetTokenRepository;
  private final EmailService emailService;
  private final AppFrontendUrlProperties appFrontendUrlProperties;

  @Transactional(readOnly = true)
  public AuthSessionResponse login(LoginRequest request) {
    String email = request.getEmail().trim().toLowerCase();

    ClientEntity client = clientService.findByEmail(email);
    if (client == null) {
      throw new InvalidCredentialsException("Credenciales inválidas");
    }

    if (!passwordEncoder.matches(request.getPassword(), client.getPasswordHash())) {
      throw new InvalidCredentialsException("Credenciales inválidas");
    }

    String token = jwtService.generateToken(client.getId(), client.getEmail(), client.getRole());

    return authMapper.toResponse(client, token);
  }

  @Transactional
  public AuthSessionResponse register(RegisterRequest request) {
    String email = request.getEmail().trim().toLowerCase();
    String phone = request.getPhone().trim();

    if (clientService.findByEmail(email) != null) {
      throw new EmailAlreadyRegisteredException("Ya existe una cuenta con ese email");
    }

    if (clientService.findByPhone(phone) != null) {
      throw new EmailAlreadyRegisteredException("Ya existe una cuenta con ese teléfono");
    }

    ClientEntity client = ClientEntity.builder()
        .name(request.getName().trim())
        .email(email)
        .joinDate(LocalDate.now())
        .passwordHash(passwordEncoder.encode(request.getPassword()))
        .role("MEMBER")
        .phone(phone)
        .birthDate(request.getBirthDate())
        .build();

    clientService.save(client);

    emailService.sendWelcomeEmail(client.getEmail(), client.getName());

    String token = jwtService.generateToken(client.getId(), client.getEmail(), client.getRole());
    return authMapper.toResponse(client, token);
  }

  @Transactional
  public void forgotPassword(String email) {
    String normalizedEmail = email.trim().toLowerCase();
    ClientEntity client = clientService.findByEmail(normalizedEmail);

    if (client == null) {
      return;
    }

    String token = UUID.randomUUID().toString();

    PasswordResetTokenEntity resetToken = PasswordResetTokenEntity.builder()
        .clientId(client.getId())
        .token(token)
        .expiration(LocalDateTime.now().plusMinutes(30))
        .used(false)
        .build();

    resetTokenRepository.save(resetToken);

    String resetLink = appFrontendUrlProperties.getFrontendUrl() + "/restablecer-clave?token=" + token;

    emailService.sendPasswordResetEmail(client.getEmail(), resetLink);
  }

 
  @Transactional
  public void resetPassword(String token, String newPassword) {
    PasswordResetTokenEntity resetToken = resetTokenRepository.findByToken(token)
        .orElseThrow(() -> new PasswordResetTokenInvalidException("Token inválido o expirado"));

    if (resetToken.isUsed()) {
      throw new PasswordResetTokenInvalidException("Este enlace ya fue utilizado");
    }

    if (resetToken.getExpiration().isBefore(LocalDateTime.now())) {
      throw new PasswordResetTokenInvalidException("El enlace ha expirado. Solicita uno nuevo.");
    }

    ClientEntity client = clientService.getEntityById(resetToken.getClientId());

    client.setPasswordHash(passwordEncoder.encode(newPassword));
    clientService.save(client);

    resetToken.setUsed(true);
    resetTokenRepository.save(resetToken);
  }
}