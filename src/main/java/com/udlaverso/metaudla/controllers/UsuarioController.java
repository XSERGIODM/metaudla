package com.udlaverso.metaudla.controllers;

import com.udlaverso.metaudla.DTOs.usuario.UsuarioDtoCreate;
import com.udlaverso.metaudla.DTOs.usuario.UsuarioResponseDto;
import com.udlaverso.metaudla.servicies.gestion_usuarios.IGestionUsuario;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin("*")
@RequiredArgsConstructor
public class UsuarioController {

    private final IGestionUsuario gestionUsuario;

    @GetMapping()
    public ResponseEntity<Page<UsuarioResponseDto>> listar(
            @RequestParam(required = false,defaultValue = "0") int pagina,
            @RequestParam(required = false,defaultValue = "10") int tamanio
    ) {
        Pageable pageable = PageRequest.of(pagina, tamanio);
        return ResponseEntity.ok(gestionUsuario.obtenerTodosLosUsuarios(pageable));
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

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> cambiarContrasena(@PathVariable Long id, @RequestBody String contrasenaVieja, @RequestBody String contrasenaNueva) {
        return ResponseEntity.ok(gestionUsuario.cambiarContrasena(id, contrasenaVieja, contrasenaNueva));
    }

    @PutMapping()
    public ResponseEntity<UsuarioResponseDto> actualizarUsuario(@RequestBody UsuarioDtoCreate usuarioDtoCreate) {
        UsuarioResponseDto usuarioResponseDto = gestionUsuario.actualizarUsuario(usuarioDtoCreate).orElse(null);
        if (usuarioResponseDto == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuarioResponseDto);
    }


    @PutMapping("/{id}/habilitar")
    public ResponseEntity<UsuarioResponseDto> habilitarUsuario(@PathVariable Long id) {
        gestionUsuario.habilitarUsuario(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/deshabilitar")
    public ResponseEntity<UsuarioResponseDto> deshabilitarUsuario(@PathVariable Long id) {
        gestionUsuario.deshabilitarUsuario(id);
        return ResponseEntity.ok().build();
    }
}
