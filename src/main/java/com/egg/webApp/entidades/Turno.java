
package com.egg.webApp.entidades;

import java.time.LocalDateTime;
import javax.persistence.*;

import com.egg.webApp.enumeraciones.Especialidad;
import lombok.Data;

@Data
@Entity
@Table(name = "turnos")
public class Turno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_turno")
    private LocalDateTime fechaTurno;

    @Enumerated(EnumType.STRING)
    private Especialidad especialidad;

    @Column(name = "motivo_consulta")
    private String motivoConsulta;
    private boolean atendido;
    private boolean turnoTomado;
    private boolean cancelado;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "profesional_id")
    private Profesional profesional;
}
