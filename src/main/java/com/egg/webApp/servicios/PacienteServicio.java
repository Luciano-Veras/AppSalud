package com.egg.webApp.servicios;


import com.egg.webApp.entidades.Imagen;
import com.egg.webApp.entidades.Paciente;
import com.egg.webApp.enumeraciones.ObraSocial;
import com.egg.webApp.enumeraciones.Rol;
import com.egg.webApp.enumeraciones.Sexo;
import com.egg.webApp.excepciones.MiExcepcion;
import com.egg.webApp.repositorios.PacienteRepositorio;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class PacienteServicio {
    private final PacienteRepositorio pacienteRepositorio;
    private final ImagenServicio imagenServicio;
    private final UsuarioServicio usuarioServicio;
    public PacienteServicio(PacienteRepositorio pacienteRepositorio, ImagenServicio imagenServicio, UsuarioServicio usuarioServicio) {
        this.pacienteRepositorio = pacienteRepositorio;
        this.imagenServicio = imagenServicio;
        this.usuarioServicio = usuarioServicio;
    }

    @Transactional
    public void registrarPaciente(String nombre, String apellido, String dni, String password, String password2, String sexo, LocalDate fechaNacimiento) throws Exception {

        validar(nombre, apellido, dni, password, password2, fechaNacimiento);
        Paciente paciente = new Paciente();
        paciente.setAltaSistema(true);
        paciente.setRol(Rol.PACIENTE);
        pacienteRepositorio.save(paciente);
        usuarioServicio.registrar(nombre, apellido, dni, password, password2, paciente.getId(), sexo, fechaNacimiento);
    }

    @Transactional
    public void actualizarPaciente(MultipartFile archivo, Long id, String email, String password, String password2, String telefono, String sexo, String obraSocial, String numeroObraSocial) throws Exception {

        Optional<Paciente> respuesta = pacienteRepositorio.findById(id);
        if (respuesta.isPresent()) {

            Paciente paciente = respuesta.get();

            paciente.setEmail(email);
            paciente.setPassword(new BCryptPasswordEncoder().encode(password));
            paciente.setRol(Rol.PACIENTE);
            paciente.setTelefono(telefono);
            paciente.setSexo(Sexo.valueOf(sexo));
            paciente.setObraSocial(ObraSocial.valueOf(obraSocial));
            paciente.setNumeroObraSocial(numeroObraSocial);

            Long idImagen = null;

            if (paciente.getImagen() != null) {
                idImagen = paciente.getImagen().getId();
            }
            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
            paciente.setImagen(imagen);

            pacienteRepositorio.save(paciente);
        }
    }

    @Transactional
    public Paciente buscarPorId(Long id){
        return pacienteRepositorio.buscarPorId(id);
    }
    public Paciente getOne(Long id) {
        return pacienteRepositorio.getOne(id);
    }

    public List<Paciente> listarPacientes() throws MiExcepcion {
        List<Paciente> pacientes = pacienteRepositorio.obtenerTodosLosPacientes();
        if (pacientes.isEmpty()) {
            throw new MiExcepcion("No hay pacientes registrados");
        } else {
            return pacientes;
        }
    }

    public void validar(String nombre, String apellido, String dni, String password, String password2, LocalDate fechaNacimiento) throws Exception {

        if (nombre.isEmpty() || nombre == null) {
            throw new Exception("El nombre no puede ser nulo o estar vacio");
        }
        if (apellido.isEmpty() || apellido == null) {
            throw new Exception("El apellidp no puede ser nulo o estar vacio");
        }
        if (dni.isEmpty() || dni == null) {
            throw new Exception("El dni no puede ser nulo o estar vacio");
        }
        if (fechaNacimiento == null) {
            throw new Exception("La fecha de nacimiento no puede ser nulo o estar vacio");
        }
        if (password.isEmpty() || password == null || password.length() <= 6) {
            throw new Exception("El password no puede estar vacio y debe contener por lo menos 6 caracteres");
        }
        if (!password.equals(password2)) {
            throw new Exception("Los password ingresados deben ser iguales");
        }
        // VALIDAR QUE DNI NO ESTÉ REPETIDO
        if (usuarioServicio.validarDNI(dni)) {
            throw new Exception("El DNI ya existe. Por favor intente nuevamente");
        }
    }

    public LocalDate convertirStringALocalDate(String fechaNacimiento) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(fechaNacimiento, formatter);
    }
    public List<Paciente> listarPacientesActivos() throws MiExcepcion {
    List<Paciente> pacientes = pacienteRepositorio.listarPacientesDeAltaEnSistema();
    
    if (pacientes.isEmpty()) {
        throw new MiExcepcion("No hay pacientes registrados");
    } else {
        return pacientes;
    }
}

}

