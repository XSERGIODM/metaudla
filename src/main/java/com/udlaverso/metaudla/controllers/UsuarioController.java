package com.udlaverso.metaudla.controllers;

import com.udlaverso.metaudla.DTOs.usuario.LoginRequestDto;
import com.udlaverso.metaudla.DTOs.usuario.LoginResponseDto;
import com.udlaverso.metaudla.DTOs.usuario.UsuarioDtoCreate;
import com.udlaverso.metaudla.DTOs.usuario.UsuarioResponseDto;
import com.udlaverso.metaudla.config.JwtService;
import com.udlaverso.metaudla.servicies.gestion_usuarios.IGestionUsuario;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin("*")
@RequiredArgsConstructor
public class UsuarioController {

    private final IGestionUsuario gestionUsuario;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @GetMapping()
    public ResponseEntity<List<UsuarioResponseDto>> listar() {
        return ResponseEntity.ok(gestionUsuario.obtenerTodosLosUsuarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> obtenerUsuario(@PathVariable Long id) {
        UsuarioResponseDto usuarioDTORespuesta = gestionUsuario.obtenerUsuarioPorId(id).orElse(null);
        if (usuarioDTORespuesta == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuarioDTORespuesta);
    }

    @PostMapping()
    public ResponseEntity<UsuarioResponseDto> crearUsuario(@RequestBody UsuarioDtoCreate usuarioDtoCreate) {
        System.out.println("Creando usuario controller: " + usuarioDtoCreate.toString());
        return ResponseEntity.ok(gestionUsuario.crearUsuario(usuarioDtoCreate));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
        System.out.println("Login controller: " + loginRequest.toString());
        // Autenticar usuario
        Optional<UsuarioResponseDto> usuarioOpt = gestionUsuario.autenticarUsuario(
            loginRequest.getUsernameOrEmail(),
            loginRequest.getContrasena()
        );

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(401).build();
        }

        UsuarioResponseDto usuario = usuarioOpt.get();

        // Generar token JWT
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
