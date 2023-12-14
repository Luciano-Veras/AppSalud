package com.egg.webApp.servicios;

import com.egg.webApp.entidades.PasswordResetToken;
import com.egg.webApp.repositorios.PasswordResetTokenRepository;
import org.springframework.stereotype.Service;

@Service
public class PasswordResetTokenService {
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public PasswordResetTokenService(PasswordResetTokenRepository passwordResetTokenRepository) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    public void guardarToken(PasswordResetToken passwordResetToken) {
        passwordResetTokenRepository.save(passwordResetToken);
    }
}
