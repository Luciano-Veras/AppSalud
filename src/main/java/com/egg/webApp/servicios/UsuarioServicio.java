package com.egg.webApp.servicios;
import com.egg.webApp.entidades.Imagen;
import com.egg.webApp.entidades.Usuario;
import com.egg.webApp.enumeraciones.Sexo;
import com.egg.webApp.excepciones.MiExcepcion;
import com.egg.webApp.repositorios.ProfesionalRepositorio;
import com.egg.webApp.repositorios.UsuarioRepositorio;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServicio implements UserDetailsService {
    private final UsuarioRepositorio usuarioRepositorio;
    private final ImagenServicio imagenServicio;
    private final ProfesionalRepositorio profesionalRepositorio;
    public UsuarioServicio(UsuarioRepositorio usuarioRepositorio, ImagenServicio imagenServicio, ProfesionalRepositorio profesionalRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.imagenServicio = imagenServicio;
        this.profesionalRepositorio = profesionalRepositorio;
    }

    @Transactional
    public void registrar(String nombre, String apellido, String dni, String password, String password2,
                          Long id, String sexo, LocalDate fechaNacimiento) throws Exception {
        validar(nombre, apellido, dni, password, password2);
        Usuario usuario = usuarioRepositorio.getOne(id);
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setFechaAlta(LocalDateTime.now());
        usuario.setDni(dni);
        usuario.setFechaNacimiento(fechaNacimiento);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setSexo(Sexo.valueOf(sexo));
        usuarioRepositorio.save(usuario);
    }

    @Transactional
    public void editarAdmin(MultipartFile archivo, String password, String password2, Long id) throws Exception {
        validarAdmin(password, password2);
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setPassword(new BCryptPasswordEncoder().encode(password));
            Long idImagen = null;
            if (usuario.getImagen() != null) {
                idImagen = usuario.getImagen().getId();
            }
            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
            usuario.setImagen(imagen);
            usuarioRepositorio.save(usuario);
        }
    }
    @Transactional
    public Usuario buscarPorId(Long id){
        return usuarioRepositorio.buscarPorId(id);
    }
    @Transactional
    public Usuario getOne(Long id) {
        Usuario usuario = usuarioRepositorio.getReferenceById(id);
        usuario.getImagen();
        return usuario;
    }

    public List<Usuario> listarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        usuarios = usuarioRepositorio.findAll();
        return usuarios;
    }

    private void validar(String nombre, String apellido, String dni, String password, String password2) throws Exception {

        if (nombre.isEmpty() || nombre == null) {
            throw new Exception("El nombre no puede ser nulo o estar vacío");
        }
        if (apellido.isEmpty() || apellido == null) {
            throw new Exception("El apellido no puede ser nulo o estar vacío");
        }
        if (dni.isEmpty() || dni == null) {
            throw new Exception("El dni no puede ser nulo o estar vacío");
        }
        if (password.isEmpty() || password == null || password.length() <= 6) {
            throw new Exception("El password no puede estar vacío y debe contener por lo menos 6 caracteres");
        }
        if (!password.equals(password2)) {
            throw new Exception("Los password ingresados deben ser iguales");
        }
    }
    private void validarAdmin(String password, String password2) throws Exception {

        if (password.isEmpty() || password == null || password.length() <= 6) {
            throw new Exception("El password no puede estar vacío y debe contener por lo menos 6 caracteres");
        }
        if (!password.equals(password2)) {
            throw new Exception("Los password ingresados deben ser iguales");
        }
    }
    @Override
    public UserDetails loadUserByUsername(String dni) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepositorio.buscarPorDni(dni);

        if (usuario != null) {

            List<GrantedAuthority> permisos = new ArrayList<>();

            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());//ROLE_USER

            permisos.add(p);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = attr.getRequest().getSession(true);

            session.setAttribute("usuariosession", usuario);

            return new User(usuario.getDni(), usuario.getPassword(), permisos);
        } else {
            return null;
        }
    }
//    public LocalDate convertirStringALocalDate(String fechaNacimiento) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//        return LocalDate.parse(fechaNacimiento, formatter);
//    }

    // METODO PARA EVITAR QUE SE INGRESEN DNIs REPETIDOS
    public Boolean validarDNI(String dni) {
        return usuarioRepositorio.existsByDni(dni);

    }

    //METODO PARA EVITAR QUE SE INGRESEN MATRICULAS REPETIDAS
    public Boolean validarMatricula(String matricula) {
        return profesionalRepositorio.existsByMatricula(matricula);
    }

    public Usuario buscarUsuarioPorDniYEmail(String dni, String email) {
        return usuarioRepositorio.buscarUsuarioPorDniYEmail(dni, email);
    }

    @Transactional
    public void actualizarPassword(String dni, String email, String password, String password2) throws MiExcepcion {
        Usuario usuario = buscarUsuarioPorDniYEmail(dni, email);
        if (usuario == null) {
            throw new MiExcepcion("No se encontró el usuario");
        }
        if (password.isEmpty() || password == null || password.length() <= 6) {
            throw new MiExcepcion("El password no puede estar vacío y debe contener por lo menos 6 caracteres");
        }
        if (!password.equals(password2)) {
            throw new MiExcepcion("Los password ingresados deben ser iguales");
        }
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuarioRepositorio.save(usuario);
    }
}