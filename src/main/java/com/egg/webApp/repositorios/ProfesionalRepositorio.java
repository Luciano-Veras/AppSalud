package com.egg.webApp.repositorios;

import com.egg.webApp.entidades.Paciente;
import com.egg.webApp.entidades.Profesional;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfesionalRepositorio extends JpaRepository<Profesional, Long> {

    @Query("SELECT p FROM Profesional p WHERE p.dni = :dni")
    public Profesional buscarPorDni(@Param("dni") String dni);

    @Query("SELECT p FROM Profesional p WHERE p.id = :id")
    public Profesional buscarPorId(@Param("id")Long id);
    @Query("SELECT p FROM Profesional p WHERE p.altaSistema = true")
    public List<Profesional> listarProfesionalesDeAltaEnSistema();
    @Query("SELECT p FROM Profesional p WHERE p.altaSistema = false")
    public List<Profesional> listarProfesionalesPendientesAlta();
    public Boolean existsByMatricula(String matricula);
   //@EntityGraph(attributePaths = {"turnosDisponibles", "calificaciones"})
    //@Query("SELECT DISTINCT p FROM Profesional p LEFT JOIN FETCH p.turnosDisponibles LEFT JOIN FETCH p.calificaciones WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :termino, '%')) OR LOWER(REPLACE(p.especialidad, ' ', '_')) LIKE LOWER(CONCAT('%', :termino, '%'))")
   //List<Profesional> buscarPorNombreOEspecialidad(@Param("termino") String termino);
   @Query("SELECT DISTINCT p FROM Profesional p WHERE LOWER(REPLACE(p.especialidad, ' ', '_')) LIKE LOWER(CONCAT('%', :especialidad, '%'))")
    List<Profesional> buscarPorNombreOEspecialidad(@Param("especialidad") String especialidad);
//    @EntityGraph(attributePaths = {"turnosDisponibles", "calificaciones"})
//    @Query("SELECT DISTINCT p FROM Profesional p LEFT JOIN FETCH p.turnosDisponibles LEFT JOIN FETCH p.calificaciones WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :termino, '%')) OR LOWER(REPLACE(p.especialidad, ' ', '_')) LIKE LOWER(CONCAT('%', :termino, '%'))")
//    List<Profesional> buscarPorNombreOEspecialidad(@Param("termino") String termino);

    @Query("SELECT p FROM Profesional p WHERE LOWER(REPLACE(p.especialidad, ' ', '_')) LIKE LOWER(CONCAT('%', :especialidad, '%'))")
    List<Profesional> buscarPorEspecialidadContiene(@Param("especialidad") String especialidad);

    //    @Query("SELECT p FROM Profesional p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :termino, '%')) OR LOWER(REPLACE(p.especialidad, ' ', '_')) LIKE LOWER(CONCAT('%', :termino, '%'))")
//    List<Profesional> buscarPorNombreOEspecialidad(@Param("termino") String termino);
    @Query("SELECT p FROM Profesional  p WHERE p.altaSistema = true")
    public List<Profesional> listarProfesionalDeAltaEnSistema();
    @Query("SELECT p FROM Profesional p WHERE p.altaSistema = false")
    public List<Profesional> listarProfesionalDeBajaEnSistema();



//consulatas para el dashboard
    @Query("SELECT COUNT(p) FROM Profesional p WHERE p.altaSistema = true")
    Long obtenerCantidadProfesionalesActivos();

    @Query("SELECT COUNT(p) FROM Profesional p WHERE p.altaSistema = false")
    Long obtenerCantidadProfesionalesInactivos();

    @Query("SELECT AVG(COALESCE(p.calificaciones.size, 0)) FROM Profesional p WHERE p.calificaciones IS NOT EMPTY")
    Double obtenerPromedioCalificacionesProfesionales();


}
