package com.udlaverso.metaudla.repositories;

import com.udlaverso.metaudla.enums.EstadoBasico;
import com.udlaverso.metaudla.enums.Rol;
import com.udlaverso.metaudla.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findUsuarioByUsername(String username);

    Optional<Usuario> findUsuarioByCorreo(String correo);

    boolean existsByUsername(String username);

    boolean existsByCorreo(String correo);

    List<Usuario> findAllByRol(Rol rol);

    List<Usuario> findUsuarioByEstado(EstadoBasico estado);

    Optional<Usuario> findByUsernameOrCorreo(String username, String correo);

}