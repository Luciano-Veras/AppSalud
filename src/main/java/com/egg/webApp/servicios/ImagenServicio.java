package com.egg.webApp.servicios;

import com.egg.webApp.entidades.Imagen;
import com.egg.webApp.excepciones.MiExcepcion;
import com.egg.webApp.repositorios.ImagenRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class ImagenServicio {
    private final ImagenRepositorio imagenRepositorio;

    public ImagenServicio(ImagenRepositorio imagenRepositorio) {
        this.imagenRepositorio = imagenRepositorio;
    }

    public Imagen guardar(MultipartFile archivo) throws MiExcepcion {
        if (archivo != null) {
            try {
                Imagen imagen = new Imagen();
                imagen.setMime(archivo.getContentType());
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());
                return imagenRepositorio.save(imagen);
            } catch (Exception e) {
                throw new MiExcepcion(e.getMessage());
            }
        }
        return null;
    }

    public Imagen actualizar(MultipartFile archivo, Long idImagen) throws MiExcepcion {
        if (archivo != null) {
            try {
                Imagen imagen = new Imagen();

                if (idImagen != null) {
                    Optional<Imagen> respuesta = imagenRepositorio.findById(idImagen);
                    if (respuesta.isPresent()) {

                        imagen = respuesta.get();
                    }
                }
                imagen.setMime(archivo.getContentType());
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());
                return imagenRepositorio.save(imagen);
            } catch (Exception e) {
                throw new MiExcepcion(e.getMessage());
            }
        }
        return null;
    }
}