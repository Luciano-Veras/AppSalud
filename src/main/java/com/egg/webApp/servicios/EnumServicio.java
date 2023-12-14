package com.egg.webApp.servicios;

import com.egg.webApp.enumeraciones.*;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class EnumServicio {
    public List<Rol> obtenerRoles() {
        return Arrays.asList(Rol.values());
    }
    public List<Sexo> obtenerGeneros(){
        return Arrays.asList(Sexo.values());
    }
    public List<Especialidad> obtenerEspecialidad() {
        return Arrays.asList(Especialidad.values());
    }
    public List<ObraSocial> obtenerObraSocial() {
        return Arrays.asList(ObraSocial.values());
    }
}
