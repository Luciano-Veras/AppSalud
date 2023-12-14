package com.egg.webApp.entidades;

import com.egg.webApp.enumeraciones.Rol;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import com.egg.webApp.enumeraciones.Sexo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Clase que representa a un usuario en la aplicación.
 * Esta clase es una entidad persistente que se mapea a la tabla "usuarios" en la base de datos.
 * Se utiliza la estrategia de herencia "JOINED" para manejar la herencia de clases.
 *
 * @version v0.1.0-alpha 07/11/2023
 * @see <a href="https://github.com/nachxg/webAppServiciosSalud/releases/tag/v0.1.0-alpha">Versión en GitHub</a>
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario {

    /**
     * Identificador único del usuario generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    /**
     * Número de identificación del usuario (DNI).
     */
    protected String dni;

    /**
     * Contraseña del usuario.
     */
    protected String password;

    /**
     * Dirección de correo electrónico del usuario.
     */
    protected String email;

    /**
     * Rol del usuario, representado como una enumeración de tipo Rol.
     */
    @Enumerated(EnumType.STRING)
    protected Rol rol;

    /**
     * Género del usuario.
     */
    @Enumerated(EnumType.STRING)
    protected Sexo sexo;

    /**
     * Fecha y hora en la que se registró al usuario.
     */
    protected LocalDateTime fechaAlta;

    /**
     * Fecha de nacimiento del usuario.
     */
    protected LocalDate fechaNacimiento;

    /**
     * Imagen asociada al usuario.
     */
    @OneToOne
    @JoinColumn(name = "imagen_id")
    protected Imagen imagen;

    /**
     * Número de teléfono del usuario.
     */
    protected String telefono;

    /**
     * Nombre del usuario.
     */
    protected String nombre;

    /**
     * Apellido del usuario.
     */
    protected String apellido;

    protected boolean altaSistema;
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<RegistroAccion> registrosAcciones;
}
