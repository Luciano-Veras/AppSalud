package com.egg.webApp.entidades;

import com.egg.webApp.enumeraciones.ObraSocial;
import com.egg.webApp.enumeraciones.Rol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pacientes")
@PrimaryKeyJoinColumn(referencedColumnName = "id")
public class Paciente extends Usuario {

    private String numeroObraSocial;

    @Enumerated(EnumType.STRING)
    private ObraSocial obraSocial;

    @OneToMany(mappedBy = "paciente", fetch = FetchType.LAZY)
    private List<Turno> turnos;

    @ManyToOne
    @JoinColumn(name = "grupo_familiar_id")
    private GrupoFamiliar grupoFamiliar;

    @ManyToOne
    @JoinColumn(name = "paciente_titular_id")
    private Paciente pacienteTitular;

    @OneToMany(mappedBy = "pacienteTitular", cascade = CascadeType.ALL)
    private List<Paciente> miembros;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL)
    private List<Calificacion> calificaciones;

    private String parentesco;


}