package com.alvar.oasisclub.common.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AppFrontendUrlProperties {

  @Value("${app.frontend-url:https://tfg-mocha.vercel.app}")
  private String frontendUrl;
}

