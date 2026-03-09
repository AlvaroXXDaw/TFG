package com.alvar.oasisclub.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class JwtService {

  @Value("${app.jwt.secret}")
  private String secret;

  @Value("${app.jwt.expiration-ms}")
  private long expirationMs;

  
  public String generateToken(UUID clientId, String email, String role) {
    Date now = new Date();
    Date expiration = new Date(now.getTime() + expirationMs);

    return Jwts.builder()
        .subject(email)
        .claim("clientId", clientId.toString())
        .claim("role", role)
        .issuedAt(now)
        .expiration(expiration)
        .signWith(getSigningKey())
        .compact();
  }

  
  public String extractEmail(String token) {
    return extractClaims(token).getSubject();
  }

  
  public String extractRole(String token) {
    return extractClaims(token).get("role", String.class);
  }

  
  public UUID extractClientId(String token) {
    String clientId = extractClaims(token).get("clientId", String.class);
    return UUID.fromString(clientId);
  }

  
  public boolean isTokenValid(String token) {
    try {
      extractClaims(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  private Claims extractClaims(String token) {
    return Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  private SecretKey getSigningKey() {
    byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}


