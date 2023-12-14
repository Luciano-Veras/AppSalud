package com.egg.webApp.entidades;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private String email;

    private LocalDateTime expirationTime;

    public PasswordResetToken(String token, String email, LocalDateTime expirationTime) {
        this.token = token;
        this.email = email;
        this.expirationTime = expirationTime;
    }
}