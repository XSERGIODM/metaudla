package com.udlaverso.metaudla.controllers;

import com.udlaverso.metaudla.models.Usuario;
import com.udlaverso.metaudla.servicies.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    // Inyección de la interfaz, no de la implementación
    @Autowired
    private IUsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        // Aquí normalmente obtendrías el usuario autenticado
        Usuario admin = new Usuario(); // Usuario temporal para ejemplo
        admin.setId(1L);

        Usuario nuevoUsuario = usuarioService.crearUsuario(usuario, admin);
        return ResponseEntity.ok(nuevoUsuario);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.buscarPorId(id);
        return usuario.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id,
                                                   @RequestBody Usuario usuario) {
        // Usuario autenticado (en un caso real vendría del contexto de seguridad)
        Usuario actualizador = new Usuario();
        actualizador.setId(1L);

        Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, usuario, actualizador);
        return ResponseEntity.ok(usuarioActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/deshabilitar")
    public ResponseEntity<Usuario> deshabilitarUsuario(@PathVariable Long id) {
        Usuario actualizador = new Usuario();
        actualizador.setId(1L);

        Usuario usuario = usuarioService.deshabilitarUsuario(id, actualizador);
        return ResponseEntity.ok(usuario);
    }

    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestParam String username,
                                       @RequestParam String password) {
        Optional<Usuario> usuario = usuarioService.autenticar(username, password);
        return usuario.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/stats/activos")
    public ResponseEntity<Long> contarUsuariosActivos() {
        long count = usuarioService.contarUsuariosActivos();
        return ResponseEntity.ok(count);
    }
}