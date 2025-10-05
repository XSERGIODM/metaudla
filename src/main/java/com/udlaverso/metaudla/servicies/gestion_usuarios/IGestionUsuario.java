package com.udlaverso.metaudla.servicies.gestion_usuarios;

import com.udlaverso.metaudla.DTO.UsuarioDTO;
import com.udlaverso.metaudla.DTO.create.CreateUsuarioDTO;
import com.udlaverso.metaudla.DTO.update.UpdateUsuarioDTO;
import com.udlaverso.metaudla.enums.EstadoBasico;
import com.udlaverso.metaudla.enums.Rol;

import java.util.List;
import java.util.Optional;

public interface IGestionUsuario {

    // ========== OPERACIONES CRUD BÁSICAS ==========

    /**
     * Crear un nuevo usuario
     * @param createUsuarioDTO Datos del usuario a crear
     * @return DTO del usuario creado
     */
    UsuarioDTO crearUsuario(CreateUsuarioDTO createUsuarioDTO);

    /**
     * Obtener usuario por ID
     * @param id ID del usuario
     * @return Optional con el DTO del usuario si existe
     */
    Optional<UsuarioDTO> obtenerUsuarioPorId(Long id);

    /**
     * Obtener usuario por username
     * @param username Username del usuario
     * @return Optional con el DTO del usuario si existe
     */
    Optional<UsuarioDTO> obtenerUsuarioPorUsername(String username);

    /**
     * Obtener usuario por correo
     * @param correo Correo del usuario
     * @return Optional con el DTO del usuario si existe
     */
    Optional<UsuarioDTO> obtenerUsuarioPorCorreo(String correo);

    /**
     * Obtener todos los usuarios
     * @return Lista de DTOs de usuarios
     */
    List<UsuarioDTO> obtenerTodosLosUsuarios();

    /**
     * Actualizar usuario existente
     * @param id ID del usuario a actualizar
     * @param updateUsuarioDTO Datos a actualizar
     * @return Optional con el DTO del usuario actualizado si existe
     */
    Optional<UsuarioDTO> actualizarUsuario(Long id, UpdateUsuarioDTO updateUsuarioDTO);

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
    List<UsuarioDTO> obtenerUsuariosPorEstado(EstadoBasico estado);

    /**
     * Obtener usuarios por rol
     * @param rol Rol de los usuarios
     * @return Lista de DTOs de usuarios con el rol especificado
     */
    List<UsuarioDTO> obtenerUsuariosPorRol(Rol rol);
}
