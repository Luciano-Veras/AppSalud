package com.egg.webApp.servicios;

import com.egg.webApp.entidades.GrupoFamiliar;
import com.egg.webApp.entidades.Paciente;
import com.egg.webApp.enumeraciones.Rol;
import com.egg.webApp.enumeraciones.Sexo;
import com.egg.webApp.repositorios.FamiliarRepositorio;
import com.egg.webApp.repositorios.PacienteRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FamiliarServicio {
    private final FamiliarRepositorio familiarRepositorio;
    private final PacienteRepositorio pacienteRepositorio;
    private final PacienteServicio pacienteServicio;

    public FamiliarServicio(FamiliarRepositorio familiarRepositorio, PacienteRepositorio pacienteRepositorio, PacienteServicio pacienteServicio) {
        this.familiarRepositorio = familiarRepositorio;
        this.pacienteRepositorio = pacienteRepositorio;
        this.pacienteServicio = pacienteServicio;
    }


    @Transactional
    public void registrarMiembro(Paciente miembro,
                                 String parentesco, String nombre, String apellido,
                                 String dni, String password, String password2,
                                 String sexo, LocalDate fechaNacimiento) throws Exception {
        try {

            pacienteServicio.validar(nombre, apellido, dni, password, password2, fechaNacimiento);

            Paciente nuevoMiembro = new Paciente();
            nuevoMiembro.setNombre(nombre);
            nuevoMiembro.setApellido(apellido);
            nuevoMiembro.setDni(dni);
            nuevoMiembro.setPassword(new BCryptPasswordEncoder().encode(password));
            nuevoMiembro.setSexo(Sexo.valueOf(sexo));
            nuevoMiembro.setFechaNacimiento(fechaNacimiento);
            nuevoMiembro.setFechaAlta(LocalDateTime.now());
            nuevoMiembro.setAltaSistema(true);
            nuevoMiembro.setRol(Rol.PACIENTE);

            // Guardar el paciente antes de asociarlo al grupo familiar
            pacienteRepositorio.save(nuevoMiembro);

            // Crear un nuevo grupo familiar si no existe uno
            GrupoFamiliar familiar = miembro.getGrupoFamiliar();
            if (familiar == null) {
                familiar = new GrupoFamiliar();
                familiar.setMiembros(new ArrayList<>());
            }

            // Asignar datos al grupo familiar y agregar el nuevo miembro
            familiar.getMiembros().add(nuevoMiembro);
            familiar.setPacienteTitular(miembro);

            // Asociar el grupo familiar al nuevo miembro y al paciente titular
            nuevoMiembro.setGrupoFamiliar(familiar);
            nuevoMiembro.setPacienteTitular(miembro);
            nuevoMiembro.setParentesco(parentesco);

            // Guardar los cambios en el grupo familiar
            familiarRepositorio.save(familiar);

        } catch (Exception e) {
            throw new Exception("Error: " + e.getMessage());
        }
    }

    public List<Object[]> listarFamiliares(Long titularId) { //Titular id == paciente id
        return familiarRepositorio.findByTitularId(titularId);

    }

    @Transactional
    public GrupoFamiliar buscarPorId(Long id) {
        return familiarRepositorio.buscarPorId(id);
    }


    /*
    public LocalDate convertirStringALocalDate(String fechaNacimiento) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(fechaNacimiento, formatter);
    }
*/
}
