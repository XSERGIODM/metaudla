package com.udlaverso.metaudla.servicies;

import com.udlaverso.metaudla.enums.EstadoBasico;
import com.udlaverso.metaudla.enums.Rol;
import com.udlaverso.metaudla.models.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {

    // CRUD Básico
    Usuario crearUsuario(Usuario usuario, Usuario creador);
    Usuario actualizarUsuario(Long id, Usuario usuarioActualizado, Usuario actualizador);
    Optional<Usuario> buscarPorId(Long id);
    Optional<Usuario> buscarPorUsername(String username);
    Optional<Usuario> buscarPorCorreo(String correo);
    List<Usuario> listarTodos();
    void eliminarUsuario(Long id);

    // Gestión de Estado
    Usuario deshabilitarUsuario(Long id, Usuario actualizador);
    Usuario habilitarUsuario(Long id, Usuario actualizador);

    // Autenticación y Seguridad
    Optional<Usuario> autenticar(String username, String password);
    void cambiarContrasena(Long id, String nuevaContrasena, Usuario actualizador);
    boolean verificarContrasena(String rawPassword, String encodedPassword);

    // Búsquedas Avanzadas
    List<Usuario> listarPorEstado(EstadoBasico estado);
    List<Usuario> listarPorRol(Rol rol);
    List<Usuario> buscarPorNombreOrUsername(String nombre, String username);

    // Estadísticas
    long contarUsuariosActivos();
    long contarUsuariosPorRol(Rol rol);
}