package com.egg.webApp.entidades;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class GrupoFamiliar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String parentesco;

    @ManyToOne
    @JoinColumn(name = "paciente_titular_id")
    private Paciente pacienteTitular;

    @OneToMany(mappedBy = "grupoFamiliar", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Paciente> miembros;


}

