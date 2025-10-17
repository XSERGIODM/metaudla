package com.udlaverso.metaudla.DTOs.usuario;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for login request
 */
@Value
public class LoginRequestDto implements Serializable {
    @NotBlank(message = "El username o correo es requerido")
    String usernameOrEmail;
    @NotBlank(message = "La contraseña es requerida")
    String contrasena;
}