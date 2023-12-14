package com.egg.webApp.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.egg.webApp.servicios.CalificacionServicio;
import com.egg.webApp.servicios.ProfesionalServicio;

@Controller
@RequestMapping("/calificaciones")
public class CalificacionesControlador {

    @GetMapping("/crear")
    public String crearCalificacion(ModelMap model) {

        return "comentario.html";
    }

}
