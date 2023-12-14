package com.egg.webApp.repositorios;

import com.egg.webApp.entidades.HistoriaClinica;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HistorialClinicoRepositorio extends JpaRepository<HistoriaClinica, Long> {

    @Query("SELECT h FROM HistoriaClinica h WHERE h.paciente.id = :id")
    public List<HistoriaClinica> buscarHistorialesPorIdPaciente(@Param("id") Long id);
    }
