package com.udlaverso.metaudla.servicies.gestion_usuarios;

import com.udlaverso.metaudla.DTOs.usuario.UsuarioDtoCreate;
import com.udlaverso.metaudla.DTOs.usuario.UsuarioResponseDto;
import com.udlaverso.metaudla.enums.EstadoBasico;
import com.udlaverso.metaudla.enums.Rol;

import java.util.List;
import java.util.Optional;

public interface IGestionUsuario {

    // ========== OPERACIONES CRUD BÁSICAS ==========

    /**
     * Crear un nuevo usuario
     * @param usuarioDtoCreate Datos del usuario a crear
     * @return DTO del usuario creado
     */
    UsuarioResponseDto crearUsuario(UsuarioDtoCreate usuarioDtoCreate);

    /**
     * Obtener usuario por ID
     * @param id ID del usuario
     * @return Optional con el DTO del usuario si existe
     */
    Optional<UsuarioResponseDto> obtenerUsuarioPorId(Long id);

    /**
     * Obtener usuario por username
     * @param username Username del usuario
     * @return Optional con el DTO del usuario si existe
     */
    Optional<UsuarioResponseDto> obtenerUsuarioPorUsername(String username);

    /**
     * Obtener usuario por correo
     * @param correo Correo del usuario
     * @return Optional con el DTO del usuario si existe
     */
    Optional<UsuarioResponseDto> obtenerUsuarioPorCorreo(String correo);

    /**
     * Obtener todos los usuarios
     * @return Lista de DTOs de usuarios
     */
    List<UsuarioResponseDto> obtenerTodosLosUsuarios();

    /**
     * Actualizar usuario existente
     * @param id ID del usuario a actualizar
     * @param updateUsuarioDTO Datos a actualizar
     * @return Optional con el DTO del usuario actualizado si existe
     */
    //Optional<UsuarioResponseDto> actualizarUsuario(Long id,);

    /**
     * Eliminar usuario por ID
     * @param id ID del usuario a eliminar
     * @return true si se eliminó correctamente, false si no existía
     */
    boolean eliminarUsuario(Long id);

    // ========== OPERACIONES ADICIONALES ==========

    /**
     * Verificar si existe usuario por ID
     * @param id ID del usuario
     * @return true si existe, false si no
     */
    boolean existeUsuarioPorId(Long id);

    /**
     * Verificar si existe usuario por username
     * @param username Username a verificar
     * @return true si existe, false si no
     */
    boolean existeUsuarioPorUsername(String username);

    /**
     * Verificar si existe usuario por correo
     * @param correo Correo a verificar
     * @return true si existe, false si no
     */
    boolean existeUsuarioPorCorreo(String correo);

    /**
     * Contar total de usuarios
     * @return Número total de usuarios
     */
    long contarUsuarios();

    /**
     * Obtener usuarios por estado
     * @param estado Estado de los usuarios
     * @return Lista de DTOs de usuarios con el estado especificado
     */
    List<UsuarioResponseDto> obtenerUsuariosPorEstado(EstadoBasico estado);

    /**
     * Obtener usuarios por rol
     * @param rol Rol de los usuarios
     * @return Lista de DTOs de usuarios con el rol especificado
     */
    List<UsuarioResponseDto> obtenerUsuariosPorRol(Rol rol);

    // ========== AUTENTICACIÓN ==========

    /**
     * Autenticar usuario con username/correo y contraseña
     * @param usernameOrEmail Username o correo del usuario
     * @param contrasena Contraseña del usuario
     * @return Optional con el DTO del usuario si las credenciales son válidas
     */
    Optional<UsuarioResponseDto> autenticarUsuario(String usernameOrEmail, String contrasena);
}
