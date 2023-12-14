package com.egg.webApp.repositorios;

import com.egg.webApp.entidades.Calificacion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CalificacionRepositorio extends JpaRepository<Calificacion, Long> {

   @Query("SELECT c FROM Calificacion c WHERE c.profesional.id = :idProfesional")
public List<Calificacion> buscarCalificacionesPorIdDeProfesionales(@Param("idProfesional") Long id);

}
