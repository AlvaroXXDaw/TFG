package com.alvar.oasisclub.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.alvar.oasisclub.auth.security.JwtService;
import java.lang.reflect.Field;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtServiceTest {

  private JwtService jwtService;

  @BeforeEach
  void setUp() throws Exception {
    jwtService = new JwtService();

    setField(jwtService, "secret", "jwt-secret-test-oasisclub-2026-min-32-chars!!");
    setField(jwtService, "expirationMs", 86400000L);
  }

  @Test
  void generateTokenReturnsNonEmptyString() {
    String token = jwtService.generateToken(UUID.randomUUID(), "test@test.com", "MEMBER");

    assertNotNull(token);
    assertFalse(token.isBlank());
  }

  @Test
  void extractEmailReturnsExpectedEmail() {
    String token = jwtService.generateToken(UUID.randomUUID(), "alvaro@test.com", "ADMIN");

    String email = jwtService.extractEmail(token);

    assertEquals("alvaro@test.com", email);
  }

  @Test
  void extractClientIdReturnsExpectedId() {
    UUID expectedClientId = UUID.randomUUID();
    String token = jwtService.generateToken(expectedClientId, "alvaro@test.com", "ADMIN");

    UUID clientId = jwtService.extractClientId(token);

    assertEquals(expectedClientId, clientId);
  }

  @Test
  void validTokenReturnsTrue() {
    String token = jwtService.generateToken(UUID.randomUUID(), "test@test.com", "MEMBER");

    assertTrue(jwtService.isTokenValid(token));
  }

  @Test
  void invalidTokenReturnsFalse() {
    assertFalse(jwtService.isTokenValid("invalid.fake.token"));
  }

  @Test
  void expiredTokenReturnsFalse() throws Exception {
    setField(jwtService, "expirationMs", 0L);
    String token = jwtService.generateToken(UUID.randomUUID(), "test@test.com", "MEMBER");

    Thread.sleep(10);

    setField(jwtService, "expirationMs", 86400000L);

    assertFalse(jwtService.isTokenValid(token));
  }

  private void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }
}

