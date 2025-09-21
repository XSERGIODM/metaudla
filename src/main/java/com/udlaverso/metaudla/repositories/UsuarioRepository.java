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

    // Búsquedas por campos únicos
    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByCorreo(String correo);

    // Verificar existencia
    boolean existsByUsername(String username);
    boolean existsByCorreo(String correo);

    // Búsquedas por estado y rol
    List<Usuario> findByEstado(EstadoBasico estado);
    List<Usuario> findByRol(Rol rol);

    // Conteos
    long countByEstado(EstadoBasico estado);
    long countByRol(Rol rol);

    // Búsquedas avanzadas
    @Query("SELECT u FROM usuario u WHERE u.estado = :estado AND u.rol = :rol")
    List<Usuario> findByEstadoAndRol(@Param("estado") EstadoBasico estado, @Param("rol") Rol rol);

    @Query("SELECT u FROM usuario u WHERE u.nombre LIKE %:nombre% OR u.username LIKE %:username%")
    List<Usuario> findByNombreOrUsername(@Param("nombre") String nombre, @Param("username") String username);

    // Usuarios con actividad reciente
    @Query("SELECT u FROM usuario u WHERE u.updatedAt > :fecha ORDER BY u.updatedAt DESC")
    List<Usuario> findUsuariosActivosRecientemente(@Param("fecha") java.time.LocalDateTime fecha);

    // Usuarios creados en un período
    @Query("SELECT u FROM usuario u WHERE u.createdAt BETWEEN :inicio AND :fin")
    List<Usuario> findUsuariosCreadosEntre(@Param("inicio") java.time.LocalDateTime inicio,
                                          @Param("fin") java.time.LocalDateTime fin);
}