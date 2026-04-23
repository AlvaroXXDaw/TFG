package com.alvar.oasisclub.auth;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.alvar.oasisclub.auth.dto.AuthSessionResponse;
import com.alvar.oasisclub.auth.dto.LoginRequest;
import com.alvar.oasisclub.auth.exception.InvalidCredentialsException;
import com.alvar.oasisclub.auth.mapper.AuthMapper;
import com.alvar.oasisclub.auth.repository.PasswordResetTokenRepository;
import com.alvar.oasisclub.auth.security.JwtService;
import com.alvar.oasisclub.auth.service.AuthService;
import com.alvar.oasisclub.common.config.AppFrontendUrlProperties;
import com.alvar.oasisclub.common.email.EmailService;
import com.alvar.oasisclub.clients.entity.ClientEntity;
import com.alvar.oasisclub.clients.service.ClientService;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

  @Mock
  private ClientService clientService;

  @Mock
  private AuthMapper authMapper;

  @Mock
  private JwtService jwtService;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private AppFrontendUrlProperties appFrontendUrlProperties;

  @Mock
  private PasswordResetTokenRepository passwordResetTokenRepository;

  @Mock
  private EmailService emailService;

  @InjectMocks
  private AuthService authService;

  private ClientEntity buildClient() {
    return ClientEntity.builder()
        .id(UUID.randomUUID())
        .name("Test")
        .email("test@example.com")
        .joinDate(LocalDate.now())
        .passwordHash("$2a$10$hashfalso")
        .role("MEMBER")
        .build();
  }

  @Test
  void loginOk() {
    LoginRequest request = new LoginRequest();
    request.setEmail("test@example.com");
    request.setPassword("oasisclub1234");

    ClientEntity client = buildClient();
    AuthSessionResponse expected = new AuthSessionResponse();
    expected.setToken("jwt-token");

    when(clientService.findByEmail("test@example.com")).thenReturn(client);
    when(passwordEncoder.matches("oasisclub1234", "$2a$10$hashfalso")).thenReturn(true);
    when(jwtService.generateToken(client.getId(), client.getEmail(), client.getRole())).thenReturn("jwt-token");
    when(authMapper.toResponse(client, "jwt-token")).thenReturn(expected);

    AuthSessionResponse result = authService.login(request);

    assertEquals("jwt-token", result.getToken());
  }

  @Test
  void loginWrongPasswordThrows() {
    LoginRequest request = new LoginRequest();
    request.setEmail("test@example.com");
    request.setPassword("wrong");

    ClientEntity client = buildClient();
    when(clientService.findByEmail("test@example.com")).thenReturn(client);
    when(passwordEncoder.matches("wrong", "$2a$10$hashfalso")).thenReturn(false);

    assertThrows(InvalidCredentialsException.class, () -> authService.login(request));
  }

  @Test
  void forgotPasswordMissingEmailDoesNothing() {
    when(clientService.findByEmail("noexiste@example.com")).thenReturn(null);

    assertDoesNotThrow(() -> authService.forgotPassword("noexiste@example.com"));
    verifyNoInteractions(passwordResetTokenRepository, emailService);
  }
}
