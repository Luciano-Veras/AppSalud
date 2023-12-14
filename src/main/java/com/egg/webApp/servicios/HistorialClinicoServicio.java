package com.egg.webApp.servicios;

import com.egg.webApp.entidades.HistoriaClinica;
import com.egg.webApp.entidades.Paciente;
import com.egg.webApp.entidades.Profesional;
import com.egg.webApp.excepciones.MiExcepcion;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.egg.webApp.repositorios.HistorialClinicoRepositorio;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class HistorialClinicoServicio {

    @Autowired
    private HistorialClinicoRepositorio historialClinicoRepositorio;
    @Autowired
    private ProfesionalServicio profesionalServicio;
    @Autowired
    private PacienteServicio pacienteServicio;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Transactional
    public void crearHistorial(String diagnostico, String tratamiento, String medicacion, String indicaciones, String estudios, String observaciones, LocalDateTime fecha, Long idPaciente, Long idProfesional) throws MiExcepcion {

        HistoriaClinica historialClinico = new HistoriaClinica();
        validar(pacienteServicio.getOne(idPaciente), profesionalServicio.getOne(idProfesional));
        historialClinico.setDiagnostico(diagnostico);
        historialClinico.setTratamiento(tratamiento);
        historialClinico.setMedicacion(medicacion);
        historialClinico.setIndicaciones(indicaciones);
        historialClinico.setEstudios(estudios);
        historialClinico.setObservaciones(observaciones);
        historialClinico.setFechaVisita(fecha);
        historialClinico.setPaciente(pacienteServicio.getOne(idPaciente));
        historialClinico.setProfesional(profesionalServicio.getOne(idProfesional));
        historialClinicoRepositorio.save(historialClinico);
    }

    @Transactional
    public void modificarHistorial(Long id, String diagnostico, String tratamiento, String medicacion, String indicaciones, String estudios, String observaciones) throws MiExcepcion {

        Optional<HistoriaClinica> respuesta = historialClinicoRepositorio.findById(id);
        if (respuesta.isPresent()) {
            HistoriaClinica historialClinico = respuesta.get();
            historialClinico.setDiagnostico(diagnostico);
            historialClinico.setTratamiento(tratamiento);
            historialClinico.setMedicacion(medicacion);
            historialClinico.setIndicaciones(indicaciones);
            historialClinico.setEstudios(estudios);
            historialClinico.setObservaciones(observaciones);
            historialClinicoRepositorio.save(historialClinico);
        }
    }

    public List<HistoriaClinica> listaDeHistorialesClinicosPorPaciente(Long idPaciente) {
        List<HistoriaClinica> historiales = new ArrayList();
        historiales = historialClinicoRepositorio.buscarHistorialesPorIdPaciente(idPaciente);
        return historiales;
    }

    public List<HistoriaClinica> listaDeHistorialesPorDNI(String dni) {
        List<HistoriaClinica> historiales = historialClinicoRepositorio.findAll();
        List<HistoriaClinica> historialesFiltrado = new ArrayList();
        for (HistoriaClinica historiale : historiales) {
            if (historiale.getPaciente().getDni().equalsIgnoreCase(dni)) {
                historialesFiltrado.add(historiale);
            }
        }
        return historialesFiltrado;
    }

    public HistoriaClinica getOne(Long id) {
        return historialClinicoRepositorio.getOne(id);
    }

    public void eliminalHistorial(Long id) {
        historialClinicoRepositorio.deleteById(id);
    }

    private void validar(Paciente paciente, Profesional profesional) throws MiExcepcion {
        if (paciente == null) {
            throw new MiExcepcion("el paciente no puede ser nulo");
        }
        if (profesional == null) {
            throw new MiExcepcion("el profecional no puede ser nulo");
        }
    }
}
