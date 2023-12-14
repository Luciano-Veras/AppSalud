package com.egg.webApp.repositorios;

import com.egg.webApp.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    @Query("SELECT u FROM Usuario u WHERE u.dni = :dni")
    public Usuario buscarPorDni(@Param("dni") String dni);

    @Query("SELECT u FROM Usuario u WHERE u.id = :id")
    public Usuario buscarPorId(@Param("id")Long id);

    public Boolean existsByDni(String dni);

    @Query("SELECT u FROM Usuario u WHERE u.dni = :dni AND u.email = :email")
    Usuario buscarUsuarioPorDniYEmail(@Param("dni") String dni, @Param("email") String email);
}
