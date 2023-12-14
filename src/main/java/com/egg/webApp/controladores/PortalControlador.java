package com.egg.webApp.controladores;

import com.egg.webApp.entidades.Usuario;
import com.egg.webApp.enumeraciones.Sexo;
import com.egg.webApp.excepciones.MiExcepcion;
import com.egg.webApp.repositorios.FamiliarRepositorio;
import com.egg.webApp.repositorios.TurnoRepositorio;
import com.egg.webApp.servicios.EnumServicio;
import com.egg.webApp.servicios.FamiliarServicio;
import com.egg.webApp.servicios.PacienteServicio;
import com.egg.webApp.servicios.ProfesionalServicio;
import com.egg.webApp.servicios.TurnoServicio;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.security.access.prepost.PreAuthorize;

@Controller
@RequestMapping("/")
public class PortalControlador {
    private final ProfesionalServicio profesionalServicio;
    private final PacienteServicio pacienteServicio;
    private final TurnoServicio turnoServicio;
    private final EnumServicio enumServicio;
    private final TurnoRepositorio turnoRepositorio;
    private final FamiliarServicio familiarServicio;

    public PortalControlador(ProfesionalServicio profesionalServicio, PacienteServicio pacienteServicio, TurnoServicio turnoServicio, EnumServicio enumServicio, TurnoRepositorio turnoRepositorio, FamiliarServicio familiarServicio) {
        this.profesionalServicio = profesionalServicio;
        this.pacienteServicio = pacienteServicio;
        this.turnoServicio = turnoServicio;
        this.enumServicio = enumServicio;
        this.turnoRepositorio = turnoRepositorio;
        this.familiarServicio = familiarServicio;
    }

    @GetMapping("/index")
    public String index() {
        return "index.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_PACIENTE', 'ROLE_ADMIN', 'ROLE_PROFESIONAL')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session, ModelMap modelo) throws MiExcepcion {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");


        // NOS TRAE UN PACIENTE PARA PASARLO A LA VISTA
        switch (logueado.getRol().toString()) {
            case "PROFESIONAL":
                modelo.addAttribute("profesional", profesionalServicio.buscarPorId(logueado.getId()));

                modelo.addAttribute("turnos", turnoServicio.listaDeTurnosDisponibles(logueado.getId()));
                return "inicioProfesional.html";
            case "ADMIN":
                return "redirect:/admin/inicio";
            default:
                modelo.addAttribute("especialidades", enumServicio.obtenerEspecialidad());

                modelo.addAttribute("profesionales", profesionalServicio.listarProfesionales());

                modelo.addAttribute("turnos", turnoServicio.listaTurnosDisponiblesValidos());

                modelo.addAttribute("paciente", pacienteServicio.buscarPorId(logueado.getId()));

                modelo.addAttribute("turnosTomados", turnoServicio.listaTurnosTomadosPorPaciente(logueado.getId()));

                modelo.addAttribute("generos", enumServicio.obtenerGeneros());

                List<Object[]> familiares = familiarServicio.listarFamiliares(logueado.getId());

                familiares = familiares.stream().filter(GrupoFamiliar -> !GrupoFamiliar[0].equals(logueado.getId())).collect(Collectors.toList()); // Metodo para filtrar el titular de la lista. Solo se muestran los pacientes con parentesco
                modelo.addAttribute("familiares", familiares);

                return "inicio.html";
        }
    }


    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {
        if (error != null) {
            modelo.put("error", "Usuario o contraseña incorrectos");
        }
        return "login.html";
    }

}