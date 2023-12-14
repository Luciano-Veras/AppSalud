package com.egg.webApp.repositorios;

import com.egg.webApp.entidades.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagenRepositorio extends JpaRepository<Imagen, Long > {

}