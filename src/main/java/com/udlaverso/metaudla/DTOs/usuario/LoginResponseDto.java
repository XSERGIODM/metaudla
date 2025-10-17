package com.udlaverso.metaudla.DTOs.usuario;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for login response
 */
@Value
public class LoginResponseDto implements Serializable {
    String token;
    UsuarioResponseDto usuario;
}