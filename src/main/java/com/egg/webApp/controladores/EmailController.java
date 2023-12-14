package com.egg.webApp.controladores;

import com.egg.webApp.email.EmailForm;
import com.egg.webApp.entidades.PasswordResetToken;
import com.egg.webApp.entidades.Usuario;
import com.egg.webApp.repositorios.PasswordResetTokenRepository;
import com.egg.webApp.servicios.EmailService;
import com.egg.webApp.servicios.UsuarioServicio;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.time.LocalDateTime;

@Controller
@SessionAttributes("verificationCode")
public class EmailController {
    private String storedCode;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    private final EmailService emailService;
    private final UsuarioServicio usuarioServicio;

    public EmailController(PasswordResetTokenRepository passwordResetTokenRepository, EmailService emailService, UsuarioServicio usuarioServicio) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.emailService = emailService;
        this.usuarioServicio = usuarioServicio;
    }

    @GetMapping("/emailForm")
    public String showEmailForm(Model model) {
        model.addAttribute("emailForm", new EmailForm());
        return "recuperoContraseña.html";
    }

    @PostMapping("/restablecer-contrasena")
    public String restablecerContrasena(@RequestParam String dni, @RequestParam String email, Model model) {
        System.out.println("DNI: " + dni + " Email: " + email);
        Usuario usuario = usuarioServicio.buscarUsuarioPorDniYEmail(dni, email);
        if (usuario != null) {
            String codigoVerificacion = emailService.enviarCodigoVerificacion(usuario.getEmail());
            System.out.println("Código de verificación generado: " + codigoVerificacion);

            // Almacena el código de verificación en el modelo
            model.addAttribute("verificationCode", codigoVerificacion);


            model.addAttribute("dni", dni);
            model.addAttribute("email", email);

            // Pasa el modelo correctamente al redirigir
            return "verificar-codigo.html";
        } else {
            model.addAttribute("error", "No se encontró un usuario con el DNI y correo electrónico proporcionados.");
            return "error.html";
        }
    }


    @PostMapping("/codigo-verificado")
    public String verificarCodigo(@RequestParam String code, Model model) {
        System.out.println("code ingresado: " + code);

        // Obtener detalles de la sesión o de la base de datos temporal
        String dni = "27136717";
        String email = "verasdiegoca78@gmail.com";
        String storedCode = (String) model.getAttribute("verificationCode");
        System.out.println("Codigo almacenado: " + storedCode);
        System.out.println("DNI: " + dni);
        System.out.println("DNI: " + dni + " Email: " + email+" Codigo: " + code+" Codigo almacenado: " + storedCode+" aqui en el verificar codigo asi el chat gpt entiende que no recupera el dni y mail si el codigo que antes era nulo ");
        System.out.println(storedCode + " aquí veo si se guarda " + code);

        // Verificar el código ingresado
        if (storedCode != null && storedCode.equals(code)) {
            // Limpiar detalles de la sesión o de la base de datos temporal
            model.addAttribute("verificationCode", null);
            Usuario usuario = usuarioServicio.buscarUsuarioPorDniYEmail(dni, email);
            model.addAttribute("userId", usuario.getId());

            // Verifica que el código de verificación no haya caducado
            PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByEmail(email);
            if (passwordResetToken != null && passwordResetToken.getExpirationTime().isBefore(LocalDateTime.now())) {
                model.addAttribute("error", "El código de verificación ha caducado.");
                return "verificar-codigo.html";
            }

            // Almacenar el código de verificación en el modelo
            model.addAttribute("verificationCode", code);

            // Redirigir a la página para restablecer la contraseña
            return "reseteo-password.html";
        } else {
            model.addAttribute("error", "Código incorrecto. Inténtalo de nuevo.");
            return "verificar-codigo.html";
        }
    }
}