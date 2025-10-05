package com.udlaverso.metaudla.repositories;

import com.udlaverso.metaudla.enums.EstadoBasico;
import com.udlaverso.metaudla.enums.Rol;
import com.udlaverso.metaudla.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findUsuarioByUsername(String username);

    Usuario findUsuarioByCorreo(String correo);

    boolean existsByUsername(String username);

    boolean existsByCorreo(String correo);

    List<Usuario> findAllByRol(Rol rol);

    List<Usuario> findUsuarioByEstado(EstadoBasico estado);
}