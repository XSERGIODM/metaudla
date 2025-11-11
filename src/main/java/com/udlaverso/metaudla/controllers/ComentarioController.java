package com.udlaverso.metaudla.controllers;

import com.udlaverso.metaudla.DTOs.ComentarioDtoCreate;
import com.udlaverso.metaudla.DTOs.ComentarioDtoResponse;
import com.udlaverso.metaudla.config.CustomUserDetails;
import com.udlaverso.metaudla.entities.Usuario;
import com.udlaverso.metaudla.enums.EstadoModeracion;
import com.udlaverso.metaudla.enums.TipoLike;
import com.udlaverso.metaudla.servicies.gestion_comentarios.IGestionComentario;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@RestController
@RequestMapping("/api/comentario")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ComentarioController {

    private final IGestionComentario gestionComentario;
    private static final Logger logger = LoggerFactory.getLogger(ComentarioController.class);

    // Método auxiliar para obtener el usuario autenticado
    private Usuario getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUsuario();
    }

    // ========== CRUD BÁSICO DE COMENTARIOS ==========

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ComentarioDtoResponse> crearComentario(
            @Validated @RequestBody ComentarioDtoCreate comentarioDto) {
        try {
            Usuario usuario = getUsuarioAutenticado();
            ComentarioDtoResponse comentario = gestionComentario.crearComentario(comentarioDto, usuario.getId());
            logger.info("Comentario creado exitosamente por usuario {}", usuario.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(comentario);
        } catch (Exception e) {
            logger.error("Error al crear comentario: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ComentarioDtoResponse> actualizarComentario(
            @PathVariable Long id,
            @Validated @RequestBody ComentarioDtoCreate comentarioDto) {
        try {
            Usuario usuario = getUsuarioAutenticado();
            ComentarioDtoResponse comentario = gestionComentario.actualizarComentario(id, comentarioDto, usuario.getId());
            logger.info("Comentario {} actualizado por usuario {}", id, usuario.getUsername());
            return ResponseEntity.ok(comentario);
        } catch (Exception e) {
            logger.error("Error al actualizar comentario {}: {}", id, e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> eliminarComentario(@PathVariable Long id) {
        try {
            Usuario usuario = getUsuarioAutenticado();
            gestionComentario.eliminarComentario(id, usuario.getId());
            logger.info("Comentario {} eliminado por usuario {}", id, usuario.getUsername());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error al eliminar comentario {}: {}", id, e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComentarioDtoResponse> obtenerComentario(@PathVariable Long id) {
        return gestionComentario.obtenerComentarioPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ========== LISTADO DE COMENTARIOS ==========

    @GetMapping("/isla/{islaId}")
    public ResponseEntity<List<ComentarioDtoResponse>> obtenerComentariosPorIsla(@PathVariable Long islaId) {
        List<ComentarioDtoResponse> comentarios = gestionComentario.obtenerComentariosPorIsla(islaId);
        return ResponseEntity.ok(comentarios);
    }

    @GetMapping("/isla/{islaId}/paginados")
    public ResponseEntity<Page<ComentarioDtoResponse>> obtenerComentariosPorIslaPaginados(
            @PathVariable Long islaId,
            @RequestParam(required = false, defaultValue = "0") int pagina,
            @RequestParam(required = false, defaultValue = "10") int tamanio) {
        Pageable pageable = PageRequest.of(pagina, tamanio);
        Page<ComentarioDtoResponse> comentarios = gestionComentario.obtenerComentariosPorIslaPaginados(islaId, pageable);
        return ResponseEntity.ok(comentarios);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ComentarioDtoResponse>> obtenerComentariosPorUsuario(@PathVariable Long usuarioId) {
        List<ComentarioDtoResponse> comentarios = gestionComentario.obtenerComentariosPorUsuario(usuarioId);
        return ResponseEntity.ok(comentarios);
    }

    // ========== SISTEMA DE LIKES ==========

    @PostMapping("/{comentarioId}/like")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> agregarLikeComentario(
            @PathVariable Long comentarioId,
            @RequestParam TipoLike tipo) {
        try {
            Usuario usuario = getUsuarioAutenticado();
            gestionComentario.agregarLikeComentario(comentarioId, usuario.getId(), tipo);
            logger.info("Like {} agregado al comentario {} por usuario {}", tipo, comentarioId, usuario.getUsername());
            return ResponseEntity.ok("Like agregado exitosamente");
        } catch (Exception e) {
            logger.error("Error al agregar like al comentario {}: {}", comentarioId, e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @DeleteMapping("/{comentarioId}/like")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> quitarLikeComentario(
            @PathVariable Long comentarioId) {
        try {
            Usuario usuario = getUsuarioAutenticado();
            gestionComentario.quitarLikeComentario(comentarioId, usuario.getId());
            logger.info("Like quitado del comentario {} por usuario {}", comentarioId, usuario.getUsername());
            return ResponseEntity.ok("Like quitado exitosamente");
        } catch (Exception e) {
            logger.error("Error al quitar like del comentario {}: {}", comentarioId, e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @GetMapping("/{comentarioId}/like/status")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> verificarLikeUsuario(
            @PathVariable Long comentarioId) {
        Usuario usuario = getUsuarioAutenticado();
        String tipoLike = gestionComentario.obtenerTipoLikeUsuario(comentarioId, usuario.getId());
        return ResponseEntity.ok(tipoLike != null ? tipoLike : "NONE");
    }

    // ========== MODERACIÓN ==========

    @PutMapping("/{comentarioId}/moderacion")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> moderarComentario(
            @PathVariable Long comentarioId,
            @RequestParam EstadoModeracion estado) {
        try {
            Usuario moderador = getUsuarioAutenticado();
            // Aquí podrías agregar validación de permisos de moderador
            gestionComentario.moderarComentario(comentarioId, estado, moderador.getId());
            logger.info("Comentario {} moderado con estado {} por {}", comentarioId, estado, moderador.getUsername());
            return ResponseEntity.ok("Comentario moderado exitosamente");
        } catch (Exception e) {
            logger.error("Error al moderar comentario {}: {}", comentarioId, e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @GetMapping("/moderacion/pendientes")
    public ResponseEntity<List<ComentarioDtoResponse>> obtenerComentariosPendientesModeracion() {
        List<ComentarioDtoResponse> comentarios = gestionComentario.obtenerComentariosPendientesModeracion();
        return ResponseEntity.ok(comentarios);
    }

    // ========== ESTADÍSTICAS ==========

    @GetMapping("/isla/{islaId}/count")
    public ResponseEntity<Long> contarComentariosPorIsla(@PathVariable Long islaId) {
        long count = gestionComentario.contarComentariosPorIsla(islaId);
        return ResponseEntity.ok(count);
    }

    // ========== RESPUESTAS ANIDADAS ==========

    @PostMapping("/{comentarioPadreId}/respuesta")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ComentarioDtoResponse> crearRespuesta(
            @PathVariable Long comentarioPadreId,
            @Validated @RequestBody ComentarioDtoCreate respuestaDto) {
        try {
            Usuario usuario = getUsuarioAutenticado();
            ComentarioDtoResponse respuesta = gestionComentario.crearRespuesta(comentarioPadreId, respuestaDto, usuario.getId());
            logger.info("Respuesta creada al comentario {} por usuario {}", comentarioPadreId, usuario.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        } catch (Exception e) {
            logger.error("Error al crear respuesta al comentario {}: {}", comentarioPadreId, e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @GetMapping("/{comentarioPadreId}/respuestas")
    public ResponseEntity<List<ComentarioDtoResponse>> obtenerRespuestasComentario(@PathVariable Long comentarioPadreId) {
        List<ComentarioDtoResponse> respuestas = gestionComentario.obtenerRespuestasComentario(comentarioPadreId);
        return ResponseEntity.ok(respuestas);
    }
}