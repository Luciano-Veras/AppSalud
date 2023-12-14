package com.egg.webApp.entidades;

import javax.persistence.*;

@Entity
public class ImagenPredeterminada {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mime;
    private String nombre;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] contenido;

    public ImagenPredeterminada() {
    }

    public ImagenPredeterminada(Long id, String mime, String nombre, byte[] contenido) {
        this.id = id;
        this.mime = mime;
        this.nombre = nombre;
        this.contenido = contenido;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public byte[] getContenido() {
        return contenido;
    }

    public void setContenido(byte[] contenido) {
        this.contenido = contenido;
    }
}
