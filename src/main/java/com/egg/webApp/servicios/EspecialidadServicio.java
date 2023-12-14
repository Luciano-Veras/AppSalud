package com.egg.webApp.servicios;

import com.egg.webApp.enumeraciones.Especialidad;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
@Service
public class EspecialidadServicio {
    public List<Especialidad> obtenerEspecialidad() {
        return Arrays.asList(Especialidad.values());
    }
}
