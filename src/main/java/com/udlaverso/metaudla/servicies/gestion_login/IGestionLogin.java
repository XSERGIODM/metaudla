package com.udlaverso.metaudla.servicies.gestion_login;

import com.udlaverso.metaudla.DTOs.usuario.UsuarioResponseDto;

import java.util.Optional;

public interface IGestionLogin {
    /**
     * Autenticar usuario con username/correo y contraseña
     * @param usernameOrEmail Username o correo del usuario
     * @param contrasena Contraseña del usuario
     * @return Optional con el DTO del usuario si las credenciales son válidas
     */
    Optional<UsuarioResponseDto> autenticarUsuario(String usernameOrEmail, String contrasena);
}
