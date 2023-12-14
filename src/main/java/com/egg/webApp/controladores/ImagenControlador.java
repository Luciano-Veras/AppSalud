package com.egg.webApp.controladores;

import com.egg.webApp.entidades.Imagen;
import com.egg.webApp.entidades.Usuario;
import com.egg.webApp.excepciones.MiExcepcion;
import com.egg.webApp.servicios.ImagenPredeterminadaServicio;
import com.egg.webApp.servicios.PacienteServicio;
import com.egg.webApp.servicios.ProfesionalServicio;
import com.egg.webApp.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controlador para las operaciones relacionadas con imágenes en la aplicación.
 * Todas las URL asociadas a este controlador comienzan con "/imagen".
 * * @version v0.1.0-alfa 11/11/2023
 */
@Controller
@RequestMapping("/imagen")
public class ImagenControlador {
    private final ImagenPredeterminadaServicio imagenPredeterminadaServicio;
    private final UsuarioServicio usuarioServicio;
    public ImagenControlador(ImagenPredeterminadaServicio imagenPredeterminadaServicio, UsuarioServicio usuarioServicio) {
        this.imagenPredeterminadaServicio = imagenPredeterminadaServicio;
        this.usuarioServicio = usuarioServicio;
    }

    @GetMapping("/perfil/{id}")
    public ResponseEntity<byte[]> imagenUsuario(@PathVariable Long id) {
        Usuario usuario = usuarioServicio.buscarPorId(id);

        if (usuario != null) {
            Imagen imagen = usuario.getImagen();
            if (imagen != null && imagen.getContenido() != null) {
                byte[] contenido = imagen.getContenido();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG);

                return new ResponseEntity<>(contenido, headers, HttpStatus.OK);
            } else {
                byte[] imagenPredeterminada = new byte[0];
                try {
                    imagenPredeterminada = imagenPredeterminadaServicio.obtenerImagenPredeterminada().getContenido();
                } catch (MiExcepcion e) {
                    System.err.println("Info Admin: " + e.getMessage());
                }
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG);
                return new ResponseEntity<>(imagenPredeterminada, headers, HttpStatus.OK);
                //return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("El usuario con ID: " + id + " no tiene una imagen asociada.").getBytes());
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("Usuario no encontrado con ID: " + id).getBytes());
        }
    }

    @GetMapping("/predeterminada")
    public String  imagenPredeterminada() {
        return "cargar-imagen-predeterminada.html";
    }

    @PostMapping("/predeterminar")
    public String registro(@RequestParam MultipartFile archivo, Model model) {
        try {
            imagenPredeterminadaServicio.guardarImagen(archivo);
            return "redirect:/inicio/";

        } catch (MiExcepcion e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/imagen/predeterminada";
        }
    }
}