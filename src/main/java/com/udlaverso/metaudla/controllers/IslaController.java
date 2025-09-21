package com.udlaverso.metaudla.controllers;

import com.udlaverso.metaudla.models.Isla;
import com.udlaverso.metaudla.models.Usuario;
import com.udlaverso.metaudla.servicies.IIslaService;
import com.udlaverso.metaudla.servicies.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/islas")
public class IslaController {

    @Autowired
    private IIslaService islaService;

    @Autowired
    private IUsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Isla> crearIsla(@RequestBody Isla isla) {
        // En un caso real, obtendrías el usuario del contexto de seguridad
        Usuario creador = usuarioService.buscarPorId(1L).orElseThrow();

        Isla nuevaIsla = islaService.crearIsla(isla, creador);
        return ResponseEntity.ok(nuevaIsla);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Isla> obtenerIsla(@PathVariable Long id) {
        Optional<Isla> isla = islaService.buscarPorId(id);
        return isla.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<Isla> obtenerIslaConEstadisticas(@PathVariable Long id) {
        Optional<Isla> isla = islaService.buscarPorIdConEstadisticas(id);
        return isla.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<Isla>> listarIslas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Isla> islas = islaService.listarConPaginacion(PageRequest.of(page, size));
        return ResponseEntity.ok(islas);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Isla>> buscarIslas(@RequestParam String query) {
        List<Isla> islas = islaService.buscarPorNombre(query);
        return ResponseEntity.ok(islas);
    }

    @GetMapping("/populares")
    public ResponseEntity<List<Isla>> obtenerMasPopulares(@RequestParam(defaultValue = "10") int limite) {
        List<Isla> islas = islaService.obtenerMasPopulares(limite);
        return ResponseEntity.ok(islas);
    }

    @GetMapping("/mejor-puntuadas")
    public ResponseEntity<List<Isla>> obtenerMejorPuntuadas(@RequestParam(defaultValue = "10") int limite) {
        List<Isla> islas = islaService.obtenerMejorPuntuadas(limite);
        return ResponseEntity.ok(islas);
    }

    @GetMapping("/recientes")
    public ResponseEntity<List<Isla>> obtenerMasRecientes(@RequestParam(defaultValue = "10") int limite) {
        List<Isla> islas = islaService.obtenerMasRecientes(limite);
        return ResponseEntity.ok(islas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Isla> actualizarIsla(@PathVariable Long id, @RequestBody Isla isla) {
        Usuario actualizador = usuarioService.buscarPorId(1L).orElseThrow();

        Isla islaActualizada = islaService.actualizarIsla(id, isla, actualizador);
        return ResponseEntity.ok(islaActualizada);
    }

    @PostMapping("/{id}/visita")
    public ResponseEntity<Void> incrementarVisita(@PathVariable Long id) {
        islaService.incrementarVisitas(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{islaId}/me-gusta")
    public ResponseEntity<Void> agregarMeGusta(@PathVariable Long islaId,
                                             @RequestParam Long usuarioId,
                                             @RequestParam String tipo) {
        // Aquí convertirías el string a TipoLike
        com.udlaverso.metaudla.enums.TipoLike tipoLike =
            "ME_GUSTA".equals(tipo) ?
            com.udlaverso.metaudla.enums.TipoLike.ME_GUSTA :
            com.udlaverso.metaudla.enums.TipoLike.NO_ME_GUSTA;

        islaService.agregarMeGusta(islaId, usuarioId, tipoLike);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{islaId}/comentario")
    public ResponseEntity<Void> agregarComentario(@PathVariable Long islaId,
                                                @RequestParam String contenido) {
        Usuario autor = usuarioService.buscarPorId(1L).orElseThrow();

        islaService.agregarComentario(islaId, contenido, autor);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{islaId}/favorito")
    public ResponseEntity<Void> agregarFavorito(@PathVariable Long islaId) {
        Usuario usuario = usuarioService.buscarPorId(1L).orElseThrow();

        islaService.agregarFavorito(islaId, usuario);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{islaId}/puntuacion")
    public ResponseEntity<Void> agregarPuntuacion(@PathVariable Long islaId,
                                                @RequestParam int calificacion) {
        Usuario usuario = usuarioService.buscarPorId(1L).orElseThrow();

        islaService.agregarPuntuacion(islaId, calificacion, usuario);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stats/globales")
    public ResponseEntity<String> obtenerEstadisticasGlobales() {
        long islasActivas = islaService.contarIslasActivas();
        long totalVisitas = islaService.contarTotalVisitas();
        long totalInteracciones = islaService.contarTotalInteracciones();

        String stats = String.format(
            "Islas activas: %d, Total visitas: %d, Total interacciones: %d",
            islasActivas, totalVisitas, totalInteracciones
        );

        return ResponseEntity.ok(stats);
    }
}