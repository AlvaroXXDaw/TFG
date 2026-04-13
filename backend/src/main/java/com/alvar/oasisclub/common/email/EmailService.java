package com.alvar.oasisclub.common.email;

import com.alvar.oasisclub.common.config.MailSenderProperties;
import jakarta.mail.internet.MimeMessage;
import java.time.Year;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService {

  private static final Logger log = LoggerFactory.getLogger(EmailService.class);

  private final JavaMailSender mailSender;
  private final MailSenderProperties mailSenderProperties;

  @Async
  public void sendPasswordResetEmail(String toEmail, String resetLink) {
    String subject = "Oasis Club | Reset Password";
    String plainText = buildResetEmailText(resetLink);
    String htmlText = buildResetEmailHtml(resetLink);
    sendEmail(toEmail, subject, plainText, htmlText, "password-reset");
  }

  @Async
  public void sendWelcomeEmail(String toEmail, String userName) {
    String subject = "Welcome to Oasis Club";
    String plainText = buildWelcomeEmailText(userName);
    String htmlText = buildWelcomeEmailHtml(userName);
    sendEmail(toEmail, subject, plainText, htmlText, "welcome");
  }

  private void sendEmail(String toEmail, String subject, String plainText, String htmlText, String type) {
    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
      String finalRecipient = resolveRecipient(toEmail);

      helper.setFrom(mailSenderProperties.getFromEmail());
      helper.setTo(finalRecipient);
      helper.setSubject(subject);
      helper.setText(plainText, htmlText);

      mailSender.send(message);
      log.info("{} email sent to {} (requested for {})", type, finalRecipient, toEmail);
    } catch (Exception e) {
      log.error("Error sending {} email to {}: {}", type, toEmail, e.getMessage(), e);
    }
  }

  private String resolveRecipient(String requestedRecipient) {
    String overrideTo = mailSenderProperties.getOverrideTo();
    if (overrideTo != null && !overrideTo.isBlank()) {
      return overrideTo.trim();
    }
    return requestedRecipient;
  }

  private String buildResetEmailText(String resetLink) {
    return """
        OASIS CLUB
        
        Recuperar clave

        Hemos recibido una solicitud para restablecer la contraseña de tu cuenta.
        Usa el siguiente enlace para crear una nueva clave de acceso:
        %s

        Este enlace expirara en 30 minutos.
        Si no solicitaste este cambio, puedes ignorar este correo con total seguridad.
        """.formatted(resetLink);
  }

  private String buildWelcomeEmailText(String userName) {
    return """
        OASIS CLUB
        
        Bienvenido, %s

        Tu cuenta en Oasis Club ha sido creada y activada correctamente.
        Ya puedes acceder a nuestra plataforma para gestionar tus reservas, explorar nuestras instalaciones y disfrutar de la experiencia premium que ofrecemos.

        Nos vemos en el club.
        """.formatted(userName);
  }

  private String buildResetEmailHtml(String resetLink) {
    String content = """
        <div style="text-align: center;">
          <h2 style="margin: 0 0 16px; font-size: 24px; font-weight: 300; letter-spacing: -0.02em; color: #18181b;">
            Recuperar tu clave
          </h2>
          <p style="margin: 0 0 32px; color: #52525b; font-size: 15px; line-height: 1.7; font-weight: 300;">
            Hemos recibido una solicitud para restablecer la contraseña de acceso a tu cuenta. Haz clic en el siguiente enlace para definir una nueva clave de forma segura.
          </p>
          <div style="margin: 36px 0;">
            <a href="%s" style="display: inline-block; background-color: #022c22; color: #ffffff; text-decoration: none; padding: 18px 40px; font-size: 13px; font-weight: 600; letter-spacing: 0.1em; text-transform: uppercase;">
              Restablecer Contraseña
            </a>
          </div>
          <p style="margin: 0 0 12px; color: #71717a; font-size: 13px; line-height: 1.6;">
            <strong>Nota de seguridad:</strong> Este enlace caducará en 30 minutos.
          </p>
          <p style="margin: 0; color: #a1a1aa; font-size: 13px; line-height: 1.6;">
            Si no has solicitado este cambio, por favor ignora este correo. Tu cuenta sigue estando protegida.
          </p>
        </div>
        """.formatted(resetLink);
    return wrapEmailLayout(content);
  }

  private String buildWelcomeEmailHtml(String userName) {
    String content = """
        <div style="text-align: center;">
          <h2 style="margin: 0 0 16px; font-size: 24px; font-weight: 300; letter-spacing: -0.02em; color: #18181b;">
            Nos alegra tenerte, %s
          </h2>
          <p style="margin: 0 0 20px; color: #52525b; font-size: 15px; line-height: 1.7; font-weight: 300;">
            Te confirmamos que tu cuenta en <strong>Oasis Club</strong> ha sido activada correctamente.
          </p>
          <p style="margin: 0 0 36px; color: #52525b; font-size: 15px; line-height: 1.7; font-weight: 300;">
            A partir de este momento puedes acceder a nuestra plataforma exclusiva para gestionar tus reservas, explorar las instalaciones y disfrutar plenamente de la experiencia premium que ofrecemos.
          </p>
          <div style="margin: 36px 0;">
            <span style="display: inline-block; border: 1px solid #022c22; color: #022c22; padding: 16px 40px; font-size: 13px; font-weight: 600; letter-spacing: 0.1em; text-transform: uppercase;">
              Membresía Activa
            </span>
          </div>
          <p style="margin: 0; color: #a1a1aa; font-size: 13px; line-height: 1.6;">
            Tu historia en Oasis Club acaba de comenzar. Esperamos verte muy pronto en nuestras instalaciones.
          </p>
        </div>
        """.formatted(userName);
    return wrapEmailLayout(content);
  }

  private String wrapEmailLayout(String content) {
    int year = Year.now().getValue();
    return """
        <!DOCTYPE html>
        <html lang="es">
        <head>
          <meta charset="utf-8">
          <meta name="viewport" content="width=device-width, initial-scale=1.0">
          <title>Oasis Club</title>
        </head>
        <body style="margin: 0; padding: 0; font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; background-color: #f4f4f5; -webkit-font-smoothing: antialiased;">
          <table width="100%%" border="0" cellspacing="0" cellpadding="0" style="background-color: #f4f4f5; padding: 40px 20px;">
            <tr>
              <td align="center">
                
                <table width="100%%" border="0" cellspacing="0" cellpadding="0" style="max-width: 600px; background-color: #ffffff; border-top: 4px solid #022c22; box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.05), 0 8px 10px -6px rgba(0, 0, 0, 0.01);">
                  
                  <tr>
                    <td style="padding: 48px 48px 0 48px; text-align: center;">
                      <div style="font-size: 20px; font-weight: 700; color: #022c22; letter-spacing: 0.3em; text-transform: uppercase; margin-bottom: 32px;">
                        OASIS CLUB
                      </div>
                      <div style="height: 1px; background-color: #e4e4e7; width: 100%%;"></div>
                    </td>
                  </tr>
        
                  <tr>
                    <td style="padding: 40px 48px;">
                      %s
                    </td>
                  </tr>
        
                  <tr>
                    <td style="padding: 0 48px 40px 48px;">
                      <div style="background-color: #f9fafb; border: 1px solid #f4f4f5; padding: 24px; text-align: center;">
                        <p style="margin: 0; color: #52525b; font-size: 13px; font-weight: 400; line-height: 1.6;">
                          ¿Tienes alguna duda o necesitas ayuda?<br>
                          Contacta con nuestro equipo de soporte en<br>
                          <a href="mailto:soporte@oasisclub.com" style="color: #022c22; text-decoration: underline; font-weight: 600;">soporte@oasisclub.com</a>
                        </p>
                      </div>
                    </td>
                  </tr>
        
                  <tr>
                    <td style="padding: 32px 48px; background-color: #022c22; text-align: center;">
                      <p style="margin: 0 0 12px; color: #d4d4d8; font-size: 11px; letter-spacing: 0.15em; text-transform: uppercase; font-weight: 600;">
                        © %d OASIS CLUB. RESERVADOS TODOS LOS DERECHOS.
                      </p>
                      <p style="margin: 0; color: #a1a1aa; font-size: 11px; line-height: 1.6;">
                        Urb. Oasis de los Alcázares · Región de Murcia, España<br>
                        Este es un mensaje automático, por favor no respondas directamente a este correo.
                      </p>
                    </td>
                  </tr>
        
                </table>
        
                <table width="100%%" border="0" cellspacing="0" cellpadding="0" style="max-width: 600px;">
                  <tr>
                    <td style="padding: 24px 0; text-align: center;">
                      <p style="margin: 0; color: #71717a; font-size: 12px;">
                        Protegemos la privacidad y seguridad de tus datos
                      </p>
                    </td>
                  </tr>
                </table>
                
              </td>
            </tr>
          </table>
        </body>
        </html>
        """.formatted(content, year);
  }
}

