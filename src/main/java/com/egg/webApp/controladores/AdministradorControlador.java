package com.egg.webApp.controladores;

import com.egg.webApp.entidades.Paciente;
import com.egg.webApp.entidades.Profesional;
import com.egg.webApp.entidades.Usuario;
import com.egg.webApp.excepciones.MiExcepcion;
import com.egg.webApp.servicios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdministradorControlador {
    private final UsuarioServicio usuarioServicio;
    private final PacienteServicio pacienteServicio;
    private final ProfesionalServicio profesionalServicio;
    private final EnumServicio enumServicio;
    private final AdministradorServicio administradorServicio;

    @Autowired
    public AdministradorControlador(UsuarioServicio usuarioServicio, PacienteServicio pacienteServicio, ProfesionalServicio profesionalServicio, EnumServicio enumServicio, AdministradorServicio administradorServicio) {
        this.usuarioServicio = usuarioServicio;
        this.pacienteServicio = pacienteServicio;
        this.profesionalServicio = profesionalServicio;
        this.enumServicio = enumServicio;
        this.administradorServicio = administradorServicio;
    }
    @GetMapping("/inicio")
    public String inicioAdmin(Model modelo)  {
        //SECTION MODAL
        modelo.addAttribute("roles", enumServicio.obtenerRoles());
        try {
            modelo.addAttribute("pacientes", pacienteServicio.listarPacientes());
        } catch (MiExcepcion e) {
            modelo.addAttribute("pacientesErrors", e.getMessage());
        }
        try {
            modelo.addAttribute("profesionales", profesionalServicio.listarProfesionales());
        } catch (MiExcepcion e) {
            modelo.addAttribute("profesionalesErrors", e.getMessage());
        }
        //SECTION INICIO
        List<Profesional> profesionalesInactivos = null;
        List<Profesional> profesionalesActivos = null;
        List<Paciente> pacientesActivos = null;
        modelo.addAttribute("usuariosRegistradosConAumento", administradorServicio.calcularUsuariosRegistradosConAumento());
        try {
            modelo.addAttribute("porcentajeIncremental", administradorServicio.calcularPorcentajeCambioFormateado());
        } catch (MiExcepcion e) {
            modelo.addAttribute("errorPorcentajeIncremental", e.getMessage());
        }
        modelo.addAttribute("usuarios", usuarioServicio.listarUsuarios());
        try {
            profesionalesInactivos = profesionalServicio.listarProfesionalesPendientesAlta();
            modelo.addAttribute("profesionalesInactivos", profesionalesInactivos);
        } catch (MiExcepcion e) {
            modelo.addAttribute("errorProfesionalInactivo", e.getMessage());
        }
        try {
            profesionalesInactivos = profesionalServicio.listarProfesionalesPendientesAlta();
            modelo.addAttribute("profesionalesInactivos", profesionalesInactivos);
        } catch (MiExcepcion e) {
            modelo.addAttribute("errorProfesionalInactivo", e.getMessage());
        }
        try {
            profesionalesActivos = profesionalServicio.listarProfesionalesActivos();
            modelo.addAttribute("profesionalesActivos", profesionalesActivos);
            modelo.addAttribute("altasProfesionales", profesionalesActivos);
            System.out.println("Profesionales activos: " + profesionalesActivos.size());
        } catch (MiExcepcion e) {
            modelo.addAttribute("errorProfesionalesActivos", e.getMessage());
        }
        try {
            pacientesActivos = pacienteServicio.listarPacientesActivos();
            modelo.addAttribute("pacientesActivos", pacientesActivos);
        } catch (MiExcepcion e) {
            modelo.addAttribute("errorPacientesActivos", e.getMessage());
        }

        return "adminInicio.html";
    }



    @GetMapping("/dashboard")
    public String listarUsuarios(ModelMap modelo) {
        modelo.addAttribute("roles", enumServicio.obtenerRoles());
        modelo.put("especialidades", enumServicio.obtenerEspecialidad());
        List<Profesional> profesionalesActivos = null;
        List<Profesional> profesionales = null;
        List<Paciente> pacientesActivos = null;
        List<Paciente> pacientes = null;
        List<Usuario> usuarios = usuarioServicio.listarUsuarios();

        try {
            modelo.put("roles", enumServicio.obtenerRoles());
            profesionales = profesionalServicio.listarProfesionales();
            modelo.addAttribute("profesionales", profesionales);
            pacientes = pacienteServicio.listarPacientes();
            modelo.addAttribute("pacientes", pacientes);
            modelo.addAttribute("generos", enumServicio.obtenerGeneros());
            modelo.addAttribute("usuarios", usuarios);
            profesionalesActivos = profesionalServicio.listarProfesionalesActivos();
            modelo.addAttribute("profesiuonalesActivos", profesionalesActivos);
            pacientesActivos = pacienteServicio.listarPacientesActivos();
            modelo.addAttribute("pacientesActivos", pacientesActivos);
        } catch (MiExcepcion e) {
            modelo.addAttribute("error", e.getMessage());
        }

        return "adminDashboard.html";
    }

    @PostMapping("/dashboard/cambiar-rol")
    public String cambiarRol(@RequestParam Long id, @RequestParam String rol, Model model) {
        try {
            administradorServicio.establecerRolUsuario(id, rol);
            return "redirect:/admin/dashboard";
        } catch (MiExcepcion e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/admin/dashboard";
        }
    }

    @GetMapping("/usuario/baja/{id}")
    public String desactivarUsuarios(@PathVariable Long id, ModelMap modelo, HttpServletRequest request) {
        try {
            administradorServicio.desactivarActivarUsuario(id);
            modelo.addAttribute("exito", "Usuario desactivado correctamente");
            String referencia = request.getHeader("Referer");
            if (referencia != null && referencia.contains("/admin/inicio")) {
                return "redirect:/admin/inicio";
            } else {
                return "redirect:/admin/dashboard";
            }
        } catch (MiExcepcion e) {
            modelo.addAttribute("error", "Mensaje Admin " + e.getMessage());
            return "redirect:/admin/dashboard";
        }
    }

    @GetMapping("/dashboard/pacientes")
    public String listarPacientes(ModelMap modelo) {
        try {
            List<Paciente> pacientes = null;
            pacientes = pacienteServicio.listarPacientes();
            modelo.addAttribute("pacientes", pacientes);
        } catch (MiExcepcion e) {
            modelo.addAttribute("error", e.getMessage());
        }
        modelo.addAttribute("generos", enumServicio.obtenerGeneros());
        return "adminDashboard.html";
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/perfil/{id}")
    public String perfilAdmin(ModelMap modelo, HttpSession session, @PathVariable Long id) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        if (usuario.getRol().toString().equalsIgnoreCase("ADMIN")) {
            usuario = usuarioServicio.buscarPorId(id);
        } else {
            usuario = (Usuario) session.getAttribute("usuariosession");
        }
        modelo.put("usuario", usuario);
        return "editarAdmin.html";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/perfil/{id}")
    public String actualizarAdmin(MultipartFile archivo, @PathVariable Long id, @RequestParam String password, @RequestParam String password2,
                                     ModelMap modelo) {
        try {
            usuarioServicio.editarAdmin(archivo, password, password2, id);
            modelo.put("exito", "Administrador actualizado correctamente");
            return "redirect:/admin/inicio";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "editarAdmin.html";
        }
    }

}
