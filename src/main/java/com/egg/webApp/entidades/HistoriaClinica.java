package com.egg.webApp.entidades;

import java.time.LocalDateTime;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "historias_clinicas")
public class HistoriaClinica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "fecha_visita")
    private LocalDateTime fechaVisita;

    private String diagnostico;
    private String tratamiento;
    private String medicacion;
    private String indicaciones;
    private String estudios;
    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;
    @ManyToOne
    @JoinColumn(name = "profesional_id")
    private Profesional profesional;
}
