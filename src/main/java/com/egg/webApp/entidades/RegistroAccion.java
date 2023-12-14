package com.egg.webApp.entidades;

import lombok.Data;

import javax.persistence.*;
@Data
@Entity
@Table(name = "registros_acciones")
public class RegistroAccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private boolean altaSistema;
}