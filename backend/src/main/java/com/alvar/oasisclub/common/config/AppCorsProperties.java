package com.alvar.oasisclub.common.config;

import java.util.List;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;

@Component
@Getter
public class AppCorsProperties {

  @Value("${app.cors.allowed-origins:https://tfg-mocha.vercel.app,http://localhost:4200,http://127.0.0.1:4200,http://localhost:4300,http://127.0.0.1:4300}")
  private List<String> allowedOrigins;

  @Value("${app.cors.allowed-origin-patterns:http://localhost:*,http://127.0.0.1:*,https://*.onrender.com,https://*.vercel.app}")
  private List<String> allowedOriginPatterns;

  public boolean isAllowedOrigin(String origin) {
    if (origin == null || origin.isBlank()) {
      return false;
    }

    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(allowedOrigins);
    config.setAllowedOriginPatterns(allowedOriginPatterns);
    return config.checkOrigin(origin) != null;
  }
}
