package com.egg.webApp.servicios;

import com.egg.webApp.entidades.PasswordResetToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class EmailService {
    private final PasswordResetTokenService passwordResetTokenService;
    private final JavaMailSender javaMailSender;
    public EmailService(PasswordResetTokenService passwordResetTokenService, JavaMailSender javaMailSender) {
        this.passwordResetTokenService = passwordResetTokenService;
        this.javaMailSender = javaMailSender;
    }

    @Value("${spring.mail.username}")
    private String emailSender;

    public void sendEmail(String from, String to, String subject, String text) {
        try {

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el correo", e);
        }
    }
    public String enviarCodigoVerificacion(String email) {
        // Generar un token y enviar un correo con el enlace para restablecer la contraseña
        String token = generarCodigoDeVerificacion();
        LocalDateTime expirationTime = LocalDateTime.now().plusHours(1);
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, email, expirationTime);

        // Utilizar directamente el repositorio para guardar el token
        passwordResetTokenService.guardarToken(passwordResetToken);
        String subject = "Código de Verificación para Restablecer Contraseña";
        String text = "Utiliza el siguiente código para restablecer tu contraseña: " + token;
        sendEmail(emailSender, email, subject, text);
        return token;
    }

    private String generarCodigoDeVerificacion() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}