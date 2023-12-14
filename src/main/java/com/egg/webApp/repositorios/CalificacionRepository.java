package com.egg.webApp.repositorios;

import com.egg.webApp.entidades.Calificacion;
import com.egg.webApp.entidades.Profesional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {
    List<Calificacion> findByProfesional(Profesional profesional);

    @Query("SELECT AVG(c.puntuacion) FROM Calificacion c WHERE c.profesional.id = :profesionalId")
    Double obtenerPromedioPuntuacionPorProfesional(@Param("profesionalId") Long profesionalId);
}
