package com.egg.webApp.repositorios;

import com.egg.webApp.entidades.RegistroAccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistroAccionRepositorio extends JpaRepository<RegistroAccion, Long> {

    List<RegistroAccion> findByAltaSistema(boolean altaSistema);
}

