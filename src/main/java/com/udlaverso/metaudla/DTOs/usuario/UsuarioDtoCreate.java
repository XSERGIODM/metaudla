package com.udlaverso.metaudla.DTOs.usuario;

import com.udlaverso.metaudla.enums.Rol;
import jakarta.validation.constraints.Pattern;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.udlaverso.metaudla.entities.Usuario}
 */
@Value
public class UsuarioDtoCreate implements Serializable {
    Long id;
    String nombre;
    String username;
    String correo;
    @Pattern(message = "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial", regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")
    String contrasena;
    Rol rol;
}