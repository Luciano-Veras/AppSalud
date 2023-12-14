package com.egg.webApp.servicios;

import com.egg.webApp.entidades.Calificacion;
import com.egg.webApp.entidades.Paciente;
import com.egg.webApp.entidades.Profesional;
import com.egg.webApp.entidades.Turno;
import com.egg.webApp.repositorios.CalificacionRepositorio;
import com.egg.webApp.repositorios.PacienteRepositorio;
import com.egg.webApp.repositorios.ProfesionalRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalificacionServicio {

    @Autowired
    private CalificacionRepositorio calificacionRepositorio;
    @Autowired
    private ProfesionalRepositorio profesionalRepositorio;
    @Autowired
    private PacienteRepositorio pacienteRepositorio;
    @Autowired
    private TurnoServicio turnoservicio;

    @Transactional
    public void crearCalificacion(String comentario, Integer calificacion, Long idTurno) {
        Turno turno = turnoservicio.getOne(idTurno);
        if (turno.isAtendido()) {
            Profesional profesional = turno.getProfesional();
            Paciente paciente = turno.getPaciente();
            if (profesional.isAltaSistema() && paciente.isAltaSistema()) {
                Calificacion nuevaCalificacion = new Calificacion();
                nuevaCalificacion.setComentario(comentario);
                nuevaCalificacion.setPuntuacion(calificacion);
                profesional.getCalificaciones().add(nuevaCalificacion);
                paciente.getCalificaciones().add(nuevaCalificacion);
                profesionalRepositorio.save(profesional);
                pacienteRepositorio.save(paciente);
                calificacionRepositorio.save(nuevaCalificacion);
            }
        }
    }

    @Transactional
    public void modificarCalificacion(Long id, String comentarioNuevo, Integer nuevaCalificacion) {
        Optional<Calificacion> respuesta = calificacionRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Calificacion calificacionModificada = respuesta.get();
            calificacionModificada.setPuntuacion(nuevaCalificacion);
            calificacionModificada.setComentario(comentarioNuevo);
            calificacionRepositorio.save(calificacionModificada);
        }
    }

    @Transactional
    public void eliminarCalificacion(Long idCalificacion) {
        Optional<Calificacion> respuesta = calificacionRepositorio.findById(idCalificacion);
        if (respuesta.isPresent()) {
            calificacionRepositorio.deleteById(idCalificacion);
        }
    }

    public List<Calificacion> calificacionesDeUnProfecional(Long idProfisional) {
        List<Calificacion> calificaciones = new ArrayList();
       calificaciones = calificacionRepositorio.buscarCalificacionesPorIdDeProfesionales(idProfisional);
        return calificaciones;
    }

    public Calificacion getOne(Long id) {
        return calificacionRepositorio.getOne(id);
    }
}
