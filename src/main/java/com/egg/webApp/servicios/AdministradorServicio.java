package com.egg.webApp.servicios;
import com.egg.webApp.entidades.Administrador;
import com.egg.webApp.entidades.Profesional;
import com.egg.webApp.entidades.RegistroAccion;
import com.egg.webApp.entidades.Usuario;
import com.egg.webApp.excepciones.MiExcepcion;
import com.egg.webApp.repositorios.RegistroAccionRepositorio;
import com.egg.webApp.repositorios.UsuarioRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AdministradorServicio {
    private final UsuarioRepositorio usuarioRepositorio;
    private final RegistroAccionRepositorio registroAccionRepositorio;
    private final ProfesionalServicio profesionalServicio;
    public AdministradorServicio(UsuarioRepositorio usuarioRepositorio, RegistroAccionRepositorio registroAccionRepositorio, ProfesionalServicio profesionalServicio) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.registroAccionRepositorio = registroAccionRepositorio;
        this.profesionalServicio = profesionalServicio;
    }

    public void desactivarActivarUsuario(Long id) throws MiExcepcion {
        System.out.println("ID: " + id);
        if (id == null) {
            throw new MiExcepcion("No se puede desactivar usuario: el ID no puede ser nulo");
        }
        Optional<Usuario> optionalUsuario = usuarioRepositorio.findById(id);
        Usuario usuario = optionalUsuario.orElseThrow(() -> new MiExcepcion("Usuario no encontrado para el ID: " + id));
        if (usuario.isAltaSistema()) {
            Administrador.darBajaUsuario(usuario);
        } else {
            Administrador.darAltaUsuario(usuario);
        }
        usuarioRepositorio.save(usuario);
        RegistroAccion registro = new RegistroAccion();
        registro.setUsuario(usuario);
        registro.setAltaSistema(usuario.isAltaSistema());
        registroAccionRepositorio.save(registro);
    }

    @Transactional
    public void establecerRolUsuario(Long id, String rol) throws MiExcepcion {
        if (id == null) {
            throw new MiExcepcion("No se puede establecer rol: el ID no puede ser nulo");
        }
        Usuario usuario = usuarioRepositorio.findById(id).orElse(null);
        assert usuario != null;
        Administrador.establecerROlUsuario(usuario, rol);
        usuarioRepositorio.save(usuario);
    }
    @Transactional
    public double[] calcularPromedioAltas() {
        List<RegistroAccion> registrosAltas = registroAccionRepositorio.findByAltaSistema(true);
        List<RegistroAccion> registrosBajas = registroAccionRepositorio.findByAltaSistema(false);
        double totalAltas = registrosAltas.size();
        double totalBajas = registrosBajas.size();
        if (totalAltas + totalBajas == 0) {
            return new double[]{0.0};
        }
        return new double[]{totalAltas / (totalAltas + totalBajas), totalAltas, totalBajas};
    }

    @Transactional
    public String calcularPorcentajeCambioFormateado() throws MiExcepcion {
        List<Profesional> profesionalesActivos = profesionalServicio.listarProfesionalesActivos();
        if (profesionalesActivos.isEmpty()) {
            return "0%";
        }
        double[] datos = calcularPromedioAltas();
        double promedio = datos[0];
        double totalAltas = datos[1];
        double totalBajas = datos[2];
        double porcentajeCambio = 0.0;
        if (totalAltas + totalBajas > 0) {
            porcentajeCambio = ((totalAltas - totalBajas) / (totalAltas + totalBajas)) * 100;
        }
        String signo = porcentajeCambio >= 0 ? "+" : "";
        return String.format("%s%.1f%%", signo, Math.abs(porcentajeCambio));
    }
    public int calcularUsuariosRegistradosConAumento() {
    long usuariosActuales = usuarioRepositorio.count();
    double aumentoPorcentaje = 0.1;
    
    // Calcular el aumento de usuarios y redondear al entero más cercano
    long aumento = Math.round(usuariosActuales * aumentoPorcentaje);
    
    // Calcular el total de nuevos usuarios
    long nuevosUsuarios = usuariosActuales + aumento;
    
    // Asegurarse de que el resultado sea un entero y devolverlo
    return Math.toIntExact(nuevosUsuarios);
}

}
