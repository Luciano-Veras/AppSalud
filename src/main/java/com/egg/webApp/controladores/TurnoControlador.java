package com.egg.webApp.controladores;

import com.egg.webApp.entidades.Turno;
import com.egg.webApp.servicios.PacienteServicio;
import com.egg.webApp.servicios.ProfesionalServicio;
import com.egg.webApp.servicios.TurnoServicio;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/turno")
public class TurnoControlador {

    @Autowired
    PacienteServicio pacienteServicio;
    @Autowired
    ProfesionalServicio profesionalServicio;
    @Autowired
    TurnoServicio turnoServicio;

    @PostMapping("/crear_turno/{id}")
    public String crearTurno(ModelMap modelo, String fecha, String hora, @PathVariable Long id, RedirectAttributes rdA,
                             HttpServletRequest request) {
        String referencia = request.getHeader("Referer");
        if (referencia != null && referencia.contains("/profesional/buscar_especialidad")) {
            return "redirect:/login";
        }

        try {
            LocalDateTime resultado = turnoServicio.convertirStringALocalDate(fecha, hora);
            Turno respuesta = turnoServicio.existeFechaHora(id, resultado);
            if (respuesta == null) {
                turnoServicio.crearTurnoDisponible(id, resultado);
                rdA.addFlashAttribute("exito",
                        "El turno se creó correctamente, y puede ser visualizado desde la sección 'Mis turnos'.");
            } else {
                rdA.addFlashAttribute("error", "El turno ya existe.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return "redirect:/inicio";
    }

    @GetMapping("/modificar_turno/{idTurno}")
    public String modificarTurno(@PathVariable Long idTurno, ModelMap modelo) {
        Turno turno = turnoServicio.getOne(idTurno);
        modelo.addAttribute("turno", turno);
        return "modificarTurnos.html";
    }

    @GetMapping("/modificar_turno/{idTurno}/{idProfesional}")
    public String modificado(ModelMap modelo, String fecha, String hora, @PathVariable Long idTurno,
                             @PathVariable Long idProfesional, RedirectAttributes rdA) {

        try {
            LocalDateTime resultado = turnoServicio.convertirStringALocalDateb(fecha, hora);
            Turno respuesta = turnoServicio.existeFechaHora(idProfesional, resultado);
            if (respuesta == null) {
                turnoServicio.modificarTurno(idTurno, resultado);
                rdA.addFlashAttribute("exito",
                        "El turno se modifico correctamente, y puede ser visualizado desde la sección 'Mis turnos'.");
            } else {
                rdA.addFlashAttribute("error", "El turno ya existe.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return "redirect:/inicio";
    }

    @GetMapping("/cancelar/profesional/{id}")
    public String cancelarTurno(RedirectAttributes rdA, @PathVariable Long id) {
        System.out.println(id);
        try {
            turnoServicio.cancelarTurno(id);
            rdA.addFlashAttribute("exito",
                    "El turno fue cancelado exitosamente y puede ser visualizado desde la sección 'Mis turnos'.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/inicio";
    }


    @GetMapping("/atendido/profesional/{id}")
    public String atendidoTurno(RedirectAttributes rdA, @PathVariable Long id) {
        System.out.println(id);
        try {
            turnoServicio.atendidoTurno(id);
            rdA.addFlashAttribute("exito",
                    "El turno fue marcado como atendido y puede ser visualizado desde la sección 'Mis turnos'.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/inicio";
    }

}