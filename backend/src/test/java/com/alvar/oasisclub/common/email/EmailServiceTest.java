package com.alvar.oasisclub.common.email;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.alvar.oasisclub.common.config.MailSenderProperties;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

  @Mock
  private JavaMailSender mailSender;

  @Mock
  private MailSenderProperties mailSenderProperties;

  @InjectMocks
  private EmailService emailService;

  @Test
  void resetEmailUsesOriginalRecipientWhenNoOverride() throws Exception {
    MimeMessage mimeMessage = createMimeMessage();

    when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
    when(mailSenderProperties.getFromEmail()).thenReturn("club@oasis.com");
    when(mailSenderProperties.getOverrideTo()).thenReturn("");

    emailService.sendPasswordResetEmail("client@example.com", "https://frontend/reset-password?token=abc");

    ArgumentCaptor<MimeMessage> messageCaptor = ArgumentCaptor.forClass(MimeMessage.class);
    verify(mailSender).send(messageCaptor.capture());
    MimeMessage sent = messageCaptor.getValue();

    assertEquals("club@oasis.com", ((InternetAddress) sent.getFrom()[0]).getAddress());
    assertEquals("client@example.com", ((InternetAddress) sent.getAllRecipients()[0]).getAddress());
    assertEquals("Oasis Club | Reset Password", sent.getSubject());
  }

  @Test
  void welcomeEmailUsesOverrideRecipient() throws Exception {
    MimeMessage mimeMessage = createMimeMessage();

    when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
    when(mailSenderProperties.getFromEmail()).thenReturn("club@oasis.com");
    when(mailSenderProperties.getOverrideTo()).thenReturn("qa@example.com");

    emailService.sendWelcomeEmail("client@example.com", "Client");

    ArgumentCaptor<MimeMessage> messageCaptor = ArgumentCaptor.forClass(MimeMessage.class);
    verify(mailSender).send(messageCaptor.capture());
    MimeMessage sent = messageCaptor.getValue();

    assertEquals("qa@example.com", ((InternetAddress) sent.getAllRecipients()[0]).getAddress());
  }

  @Test
  void welcomeEmailFailureIsHandled() {
    when(mailSender.createMimeMessage()).thenThrow(new RuntimeException("smtp down"));

    assertDoesNotThrow(() -> emailService.sendWelcomeEmail("client@example.com", "Client"));
    verify(mailSender, never()).send(any(MimeMessage.class));
  }

  private MimeMessage createMimeMessage() {
    return new MimeMessage(Session.getInstance(new Properties()));
  }
}

