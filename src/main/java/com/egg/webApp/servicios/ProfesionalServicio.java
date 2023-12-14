package com.egg.webApp.servicios;

import com.egg.webApp.entidades.*;
import com.egg.webApp.enumeraciones.Especialidad;
import com.egg.webApp.enumeraciones.Rol;
import com.egg.webApp.enumeraciones.Sexo;
import com.egg.webApp.excepciones.MiExcepcion;
import com.egg.webApp.repositorios.FamiliarRepositorio;
import com.egg.webApp.repositorios.PacienteRepositorio;
import com.egg.webApp.repositorios.ProfesionalRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.sql.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProfesionalServicio {
    private final ProfesionalRepositorio profesionalRepositorio;
    private final ImagenServicio imagenServicio;
    private final FamiliarRepositorio familiarRepositorio;
    private final UsuarioServicio usuarioServicio;

    public ProfesionalServicio(ProfesionalRepositorio profesionalRepositorio, ImagenServicio imagenServicio, FamiliarRepositorio familiarRepositorio, UsuarioServicio usuarioServicio) {
        this.profesionalRepositorio = profesionalRepositorio;
        this.imagenServicio = imagenServicio;
        this.familiarRepositorio = familiarRepositorio;
        this.usuarioServicio = usuarioServicio;
    }

    @Transactional
    public void registrarProfesional(String nombre, String apellido, String dni, String password, String password2, String sexo, String matricula, String especialidad, LocalDate fechaNacimiento) throws Exception {

        validar(nombre, apellido, dni, password, password2, matricula, especialidad);
        Profesional profesional = new Profesional();
        profesional.setMatricula(matricula);
        profesional.setEspecialidad(Especialidad.valueOf(especialidad));
        profesional.setRol(Rol.PROFESIONAL);
        profesionalRepositorio.save(profesional);
        usuarioServicio.registrar(nombre, apellido, dni, password, password2, profesional.getId(), sexo, fechaNacimiento);
    }

    @Transactional
    public void actualizarProfesional(MultipartFile archivo, Long id, String email, String password,
                                      String password2, String telefono, String sexo) throws Exception {
        validarActualizacion(password, password2, sexo, telefono, email);
        Optional<Profesional> respuesta = profesionalRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Profesional profesional = respuesta.get();
            profesional.setEmail(email);
            profesional.setPassword(new BCryptPasswordEncoder().encode(password));
            profesional.setTelefono(telefono);
            profesional.setSexo(Sexo.valueOf(sexo));
            Long idImagen = null;
            if (profesional.getImagen() != null) {
                idImagen = profesional.getImagen().getId();
            }

            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
            profesional.setImagen(imagen);
            profesionalRepositorio.save(profesional);
        }
    }

    public Profesional getOne(Long id) {
        Profesional profesional = profesionalRepositorio.getOne(id);
        return profesional;
    }

    public List<Profesional> listarProfesionales() throws MiExcepcion {
        List<Profesional> Profesionales = new ArrayList<>();
        Profesionales = profesionalRepositorio.findAll();
        if(Profesionales.isEmpty()){
            throw new MiExcepcion("No hay profesionales disponibles");
        }
        return Profesionales;
    }

    public List<Profesional> listarProfesionalesActivos() throws MiExcepcion {
        List<Profesional> profesionales = new ArrayList<>();
        profesionales = profesionalRepositorio.listarProfesionalesDeAltaEnSistema();
        if (profesionales.isEmpty()) {
            throw new MiExcepcion("No hay pacientes registrados");
        } else {
            return profesionales;
        }
    }


    public List<Profesional> listarProfesionalesPendientesAlta() throws MiExcepcion {
        List<com.egg.webApp.entidades.Profesional> profesionales = new ArrayList<>();
        profesionales = profesionalRepositorio.listarProfesionalesPendientesAlta();
        if (profesionales.isEmpty()) {
            throw new MiExcepcion("No hay profesionales pendientes de alta en el sistema.");
        } else {
            return profesionales;
        }
    }

    private void validar(String nombre, String apellido, String dni, String password,
                         String password2, String matricula, String especialidad) throws Exception {
        if (nombre.isEmpty() || nombre == null) {
            throw new Exception("El nombre no puede ser nulo o estar vacio");
        }
        if (apellido.isEmpty() || apellido == null) {
            throw new Exception("El apellido no puede ser nulo o estar vacio");
        }
        if (dni.isEmpty() || dni == null) {
            throw new Exception("El dni no puede ser nulo o estar vacio");
        }
        if (password.isEmpty() || password == null || password.length() <= 6) {
            throw new Exception("El password no puede estar vacio y debe contener por lo menos 6 caracteres");
        }
        if (!password.equals(password2)) {
            throw new Exception("Los password ingresados deben ser iguales");
        }
        if (matricula.isEmpty() || matricula == null) {
            throw new Exception("La matricula no puede ser nulo o estar vacio");
        }
        if (especialidad.isEmpty() || especialidad == null) {
            throw new Exception("La especialidad no puede ser nulo o estar vacio");
        }
        // VALIDAR QUE DNI NO ESTÉ REPETIDO
        if (usuarioServicio.validarDNI(dni)) {
            throw new Exception("El DNI ya existe. Por favor intente nuevamente");
        }
        //VALIDAR QUE LA MATRICULA NO ESTÉ REPETIDA
        if (usuarioServicio.validarMatricula(matricula)) {
            throw new Exception("La matricula ingresada ya existe. Por favor intente nuevamente");
        }


    }

    private void validarActualizacion(String password, String password2, String sexo,
                                      String telefono, String email) throws Exception {
        if (sexo.isEmpty() || sexo == null) {
            throw new Exception("El sexo no puede ser nulo o estar vacio");
        }
        if (telefono.isEmpty() || telefono == null) {
            throw new Exception("El telefono no puede ser nulo o estar vacio");
        }
        if (email.isEmpty() || email == null) {
            throw new Exception("El email no puede ser nulo o estar vacio");
        }
        if (password.isEmpty() || password == null || password.length() <= 6) {
            throw new Exception("El password no puede estar vacio y debe contener por lo menos 6 caracteres");
        }
        if (!password.equals(password2)) {
            throw new Exception("Los password ingresados deben ser iguales");
        }

    }

    public LocalDate convertirStringALocalDate(String fechaNacimiento) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(fechaNacimiento, formatter);
    }

    public List<GrupoFamiliar> listarFamiliar() {

        List<GrupoFamiliar> familiares = new ArrayList<>();
        familiares = familiarRepositorio.findAll();
        return familiares;

    }

    public List<Profesional> buscarPorEspecialidad(String especialidad) throws MiExcepcion {
        if (especialidad.isEmpty() || especialidad == null) {
            throw new MiExcepcion("Indique una especialidad");
        }
        return profesionalRepositorio.buscarPorEspecialidadContiene(especialidad);
    }

    @Transactional
    public List<Profesional> buscarPorNombreOEspecialidad(String termino) throws MiExcepcion {
        if (termino.isEmpty() || termino == null) {
            throw new MiExcepcion("Indique un nombre o especialidad");
        }
        return profesionalRepositorio.buscarPorNombreOEspecialidad(termino);
    }

    public List<Profesional> listarProfesionalesBaja() throws MiExcepcion {
        List<Profesional> profesionales = profesionalRepositorio.listarProfesionalDeBajaEnSistema();

        if (profesionales.isEmpty()) {
            throw new MiExcepcion("No hay más profesionales disponibles para dar de alta en el sistema");
        }
        return profesionales;
    }

    @Transactional
    public Profesional buscarPorId(Long id) {
        return profesionalRepositorio.buscarPorId(id);
    }
}
