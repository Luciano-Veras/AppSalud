package com.egg.webApp.servicios;

import com.egg.webApp.entidades.Paciente;
import com.egg.webApp.entidades.Profesional;
import com.egg.webApp.entidades.Turno;
import com.egg.webApp.repositorios.ProfesionalRepositorio;
import com.egg.webApp.repositorios.TurnoRepositorio;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TurnoServicio {

    @Autowired
    private TurnoRepositorio turnoRepositorio;
    @Autowired
    private PacienteServicio pacienteServicio;
    @Autowired
    private ProfesionalRepositorio profesionalRepositorio;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Transactional
    public void crearTurnoDisponible(Long idProfesional, LocalDateTime fecha) {

        Turno nuevoTurno = new Turno();
        Profesional profesional = profesionalRepositorio.getById(idProfesional);

        if (profesional.isAltaSistema()) {
            nuevoTurno.setProfesional(profesional);
            nuevoTurno.setFechaTurno(fecha);
            nuevoTurno.setAtendido(false);
            nuevoTurno.setCancelado(false);
            nuevoTurno.setEspecialidad(profesional.getEspecialidad());
            nuevoTurno.setMotivoConsulta("Motivo de consulta");
            nuevoTurno.setPaciente(null);
            profesional.getTurnosDisponibles().add(nuevoTurno);
            profesionalRepositorio.save(profesional);
        }
    }

    @Transactional
    public void modificarTurno(Long idTurno, LocalDateTime fecha) {
        Optional<Turno> respuesta = turnoRepositorio.findById(idTurno);
        if (respuesta.isPresent()) {
            Turno turno = respuesta.get();
            turno.setFechaTurno(fecha);
            turnoRepositorio.save(turno);
        }
    }

    @Transactional
    public void cancelarTurno(Long id) {
        Turno turno = turnoRepositorio.buscarTurnosPorId(id);

        if (turno != null) {
            turno.setCancelado(true);
            turnoRepositorio.save(turno);
        } else {
            System.out.println("No encontro Turno");
        }
    }

    @Transactional
    public void atendidoTurno(Long id) {
        Turno turno = turnoRepositorio.buscarTurnosPorId(id);

        if (turno != null) {
            turno.setAtendido(true);
            turnoRepositorio.save(turno);
        } else {
            System.out.println("No encontro Turno");
        }
    }

    @Transactional
    public void tomarUnTurnoPaciente(Long idPaciente, Long idTurno, String motivoConsulta) {

        Paciente paciente = pacienteServicio.getOne(idPaciente);
        Turno turno = turnoRepositorio.getOne(idTurno);
        if (paciente.isAltaSistema() && !turno.isAtendido() && !turno.isCancelado()) {
            turno.setPaciente(paciente);
            turno.setMotivoConsulta(motivoConsulta);
            turnoRepositorio.save(turno);
        }
    }

    public List<Turno> listaDeTurnosDisponibles(Long idProfesional) {
        List<Turno> turnos = turnoRepositorio.buscarTurnosDisponiblesDeProfesional(idProfesional);
        return turnos;
    }

    public Turno existeFechaHora(Long idProfesional, LocalDateTime fechaHora) throws Exception {
        return turnoRepositorio.existeFechaHora(idProfesional, fechaHora);
    }

    public List<Turno> listaTurnosTomadosPorPaciente(Long idPaciente) {
        return turnoRepositorio.buscarTurnosPorIdPaciente(idPaciente);
    }

    public List<Turno> listaTurnosDisponiblesValidos() {
        List<Turno> turnos = turnoRepositorio.findAll();
        List<Turno> turnosValidos = new ArrayList<>();

        for (Turno turno : turnos) {
            if (turno.getPaciente() == null) {
                turnosValidos.add(turno);
            }
        }

        return turnosValidos;
    }

    public Turno getOne(Long id) {
        return turnoRepositorio.getOne(id);
    }

    public LocalDateTime convertirStringALocalDate(String fecha, String hora) {

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        String fechaHoraString = fecha + " " + hora;

        return LocalDateTime.parse(fechaHoraString, formato);

    }

    public LocalDateTime convertirStringALocalDateb(String fecha, String hora) {

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String fechaHoraString = fecha + " " + hora;

        return LocalDateTime.parse(fechaHoraString, formato);

    }

}