package com.udlaverso.metaudla.controllers;

import com.udlaverso.metaudla.DTOs.isla.IslaDtoResponse;
import com.udlaverso.metaudla.DTOs.usuario.UsuarioResponseDto;
import com.udlaverso.metaudla.servicies.gestion_islas.IGestionIsla;
import com.udlaverso.metaudla.servicies.gestion_usuarios.IGestionUsuario;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prueba")
@CrossOrigin("*")
@RequiredArgsConstructor
public class PruebaController {

    private final IGestionUsuario gestionUsuario;
    private final IGestionIsla gestionIsla;

    @GetMapping("/listar")
    public ResponseEntity<List<UsuarioResponseDto>> listar() {
        return ResponseEntity.ok(gestionUsuario.obtenerTodosLosUsuarios());
    }
    @GetMapping("/listar/isla")
    public ResponseEntity<List<IslaDtoResponse>> listarIsla() {
        return ResponseEntity.ok(gestionIsla.obtenerTodasIslas());
    }
}
