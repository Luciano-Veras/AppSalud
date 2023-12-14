package com.egg.webApp.repositorios;

import com.egg.webApp.entidades.GrupoFamiliar;
import com.egg.webApp.entidades.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FamiliarRepositorio extends JpaRepository<GrupoFamiliar, Long> {

    @Query("SELECT p FROM GrupoFamiliar p WHERE p.id = :id")
    public GrupoFamiliar buscarPorId(@Param("id") Long id);

    @Query("SELECT p.id, p.nombre, p.apellido, p.dni, p.parentesco FROM Paciente p " +
            "LEFT JOIN p.grupoFamiliar gf " +
            "WHERE gf.pacienteTitular.id = :titularId AND p.id <> :titularId")
    List<Object[]> findByTitularId(@Param("titularId") Long titularId); //Lista de objetos para poder excluir al titular de la lista

}
