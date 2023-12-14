package com.egg.webApp.repositorios;

import com.egg.webApp.entidades.ImagenPredeterminada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagenPredeterminadaRepository extends JpaRepository<ImagenPredeterminada, Long> {
}
