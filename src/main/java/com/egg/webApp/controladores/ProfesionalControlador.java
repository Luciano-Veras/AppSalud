package com.egg.webApp.controladores;

import com.egg.webApp.entidades.Profesional;
import com.egg.webApp.entidades.Usuario;
import com.egg.webApp.enumeraciones.Especialidad;
import com.egg.webApp.enumeraciones.Sexo;
import com.egg.webApp.excepciones.MiExcepcion;
import com.egg.webApp.servicios.EnumServicio;
import com.egg.webApp.servicios.ProfesionalServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/profesional")
public class ProfesionalControlador {
    @Autowired
    ProfesionalServicio profesionalServicio;
    @Autowired
    EnumServicio enumServicio;

    @GetMapping("/registrar")
    public String registrar(ModelMap modelo) {

        List<Sexo> generos = enumServicio.obtenerGeneros();
        modelo.addAttribute("generos", generos);

        List<Especialidad> especialidades = enumServicio.obtenerEspecialidad();
        modelo.addAttribute("especialidades", especialidades);

        return "registrar_profesional.html";
    }

    @PostMapping("/registrar")
    public String registro(@RequestParam String nombre, @RequestParam String apellido, @RequestParam String password,
                           @RequestParam String password2, @RequestParam String dni, @RequestParam String sexo,
                           @RequestParam String matricula, ModelMap modelo,
                           @RequestParam String especialidad,
                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaNacimiento) {
        try {

            profesionalServicio.registrarProfesional(nombre, apellido, dni, password, password2, sexo, matricula,
                    especialidad, fechaNacimiento);

            return "redirect:/index";

        } catch (Exception e) {
            System.out.println("ERROR ERROR USUARIO NO CREADO");
            System.out.println(e.getMessage());
            return "redirect:/profesional/registrar";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_PROFESIONAL', 'ROLE_ADMIN')")
    @GetMapping("/perfil/{id}")
    public String perfilProfesional(ModelMap modelo, HttpSession session, @PathVariable Long id) {

        List<Sexo> generos = enumServicio.obtenerGeneros();
        modelo.addAttribute("generos", generos);

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        Profesional profesional = null;

        if (usuario.getRol().toString().equalsIgnoreCase("ADMIN")) {
            profesional = profesionalServicio.getOne(id);
        } else {
            profesional = (Profesional) session.getAttribute("usuariosession");
        }

        modelo.put("profesional", profesional);
        return "editarProfesional.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_PROFESIONAL', 'ROLE_ADMIN')")
    @PostMapping("/perfil/{id}")
    public String actualizarProfesional(MultipartFile archivo, @PathVariable Long id, @RequestParam String email,
                                        @RequestParam String password, @RequestParam String password2, ModelMap modelo,
                                        @RequestParam String telefono, @RequestParam String sexo) {

        try {
            profesionalServicio.actualizarProfesional(archivo, id, email, password, password2, telefono,
                    sexo.toUpperCase());
            modelo.put("exito", "Profesional actualizado con exito");
            return "redirect:/inicio";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "editarProfesional.html";
        }
    }

    @GetMapping("/especialidad")
    public String especialidad(ModelMap modelo) {
        modelo.addAttribute("especialidades", enumServicio.obtenerEspecialidad());
        return "especialidades.html";
    }

    @PostMapping("/buscar_especialidad")
    public String buscarEspecialidad(ModelMap modelo, @RequestParam String termino) {
        List<Profesional> profesionales = null;
        try {
            profesionales = profesionalServicio.buscarPorNombreOEspecialidad(termino);
            if (profesionales.isEmpty()) {
                modelo.addAttribute("mensaje", "No se encontraron profesionales para la especialidad");
            } else {


            }
            modelo.addAttribute("profesionales", profesionales);
            return "medicos.html";

        } catch (MiExcepcion e) {
            modelo.addAttribute("mensaje", e.getMessage());
            return "redirect:/profesional/especialidad";
        }
    }
    }