package com.udlaverso.metaudla.controllers;

import com.udlaverso.metaudla.DTOs.usuario.LoginRequestDto;
import com.udlaverso.metaudla.DTOs.usuario.LoginResponseDto;
import com.udlaverso.metaudla.DTOs.usuario.UsuarioResponseDto;
import com.udlaverso.metaudla.config.JwtService;
import com.udlaverso.metaudla.servicies.gestion_login.IGestionLogin;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
@CrossOrigin("*")
public class LoginController {

    private final JwtService jwtService;
    private final IGestionLogin gestionLogin;

    @PostMapping()
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequest) {

        Optional<UsuarioResponseDto> usuarioOpt = gestionLogin.autenticarUsuario(
                loginRequest.getUsernameOrEmail(),
                loginRequest.getContrasena()
        );

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(401).build();
        }

        UsuarioResponseDto usuario = usuarioOpt.get();

        String token = jwtService.generateToken(
                new org.springframework.security.core.userdetails.User(
                        usuario.getUsername(),
                        "",
                        java.util.Collections.emptyList()
                )
        );

        LoginResponseDto response = new LoginResponseDto(token, usuario);
        return ResponseEntity.ok(response);
    }
}
