package com.alvar.oasisclub.common.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class MailSenderProperties {

  @Value("${spring.mail.username}")
  private String fromEmail;

  @Value("${app.mail.override-to:}")
  private String overrideTo;
}

