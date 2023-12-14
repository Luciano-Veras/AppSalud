package com.egg.webApp.repositorios;

import com.egg.webApp.entidades.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PacienteRepositorio extends JpaRepository<Paciente, Long> {


    @Query("SELECT p FROM Paciente p WHERE p.dni = :dni")
    public Paciente buscarPorDni(@Param("dni") String dni);

    @Query("SELECT p FROM Paciente p WHERE p.id = :id")
    public Paciente buscarPorId(@Param("id")Long id);

    @Query("SELECT p FROM Paciente p WHERE p.altaSistema = true")
    public List<Paciente> listarPacientesDeAltaEnSistema();

    @Query("SELECT p FROM Paciente p")
    List<Paciente> obtenerTodosLosPacientes();

    @Query("SELECT p FROM Paciente p WHERE p.altaSistema = false")
    public List<Paciente> listarPacientesDeBajaEnSistema();

}
