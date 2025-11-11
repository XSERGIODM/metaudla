package com.udlaverso.metaudla.controllers;

import com.udlaverso.metaudla.config.CustomUserDetails;
import com.udlaverso.metaudla.entities.Favorito;
import com.udlaverso.metaudla.entities.Isla;
import com.udlaverso.metaudla.entities.MeGusta;
import com.udlaverso.metaudla.entities.Puntuacion;
import com.udlaverso.metaudla.entities.Usuario;
import com.udlaverso.metaudla.enums.TipoLike;
import com.udlaverso.metaudla.repositories.FavoritoRepository;
import com.udlaverso.metaudla.repositories.IslaRepository;
import com.udlaverso.metaudla.repositories.MeGustaRepository;
import com.udlaverso.metaudla.repositories.PuntuacionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/api/interacciones")
@CrossOrigin("*")
@RequiredArgsConstructor
public class MeGustaFavoritoController {

    private final MeGustaRepository meGustaRepository;
    private final FavoritoRepository favoritoRepository;
    private final IslaRepository islaRepository;
    private final PuntuacionRepository puntuacionRepository;
    private static final Logger logger = LoggerFactory.getLogger(MeGustaFavoritoController.class);

    private Usuario getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUsuario();
    }

    @PostMapping("/isla/{islaId}/like")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> agregarMeGusta(@PathVariable Long islaId) {
        try {
            Usuario usuario = getUsuarioAutenticado();

            // Verificar que la isla existe
            Isla isla = islaRepository.findById(islaId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Isla no encontrada"));

            // Verificar si ya existe un me gusta
            if (meGustaRepository.existsByUsuarioIdAndIslaId(usuario.getId(), islaId)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Ya has dado me gusta a esta isla");
            }

            // Crear el me gusta
            MeGusta meGusta = new MeGusta();
            meGusta.setUsuario(usuario);
            meGusta.setIsla(isla);
            meGusta.setTipo(TipoLike.ME_GUSTA);

            meGustaRepository.save(meGusta);

            logger.info("Me gusta agregado exitosamente para usuario {} en isla {}", usuario.getId(), islaId);
            return ResponseEntity.ok("Me gusta agregado exitosamente");

        } catch (ResponseStatusException e) {
            logger.error("Error al agregar me gusta: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error interno al agregar me gusta: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", e);
        }
    }

    @DeleteMapping("/isla/{islaId}/like")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> quitarMeGusta(@PathVariable Long islaId) {
        try {
            Usuario usuario = getUsuarioAutenticado();

            // Buscar el me gusta existente
            MeGusta meGusta = meGustaRepository.findByUsuarioIdAndIslaId(usuario.getId(), islaId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No has dado me gusta a esta isla"));

            // Eliminar el me gusta
            meGustaRepository.delete(meGusta);

            logger.info("Me gusta eliminado exitosamente para usuario {} en isla {}", usuario.getId(), islaId);
            return ResponseEntity.ok("Me gusta eliminado exitosamente");

        } catch (ResponseStatusException e) {
            logger.error("Error al quitar me gusta: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error interno al quitar me gusta: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", e);
        }
    }

    @PostMapping("/isla/{islaId}/favorito")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> agregarFavorito(@PathVariable Long islaId) {
        try {
            Usuario usuario = getUsuarioAutenticado();

            // Verificar que la isla existe
            Isla isla = islaRepository.findById(islaId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Isla no encontrada"));

            // Verificar si ya existe un favorito
            if (favoritoRepository.existsByUsuarioIdAndIslaId(usuario.getId(), islaId)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Ya has agregado esta isla a favoritos");
            }

            // Crear el favorito
            Favorito favorito = new Favorito();
            favorito.setUsuario(usuario);
            favorito.setIsla(isla);

            favoritoRepository.save(favorito);

            logger.info("Favorito agregado exitosamente para usuario {} en isla {}", usuario.getId(), islaId);
            return ResponseEntity.ok("Favorito agregado exitosamente");

        } catch (ResponseStatusException e) {
            logger.error("Error al agregar favorito: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error interno al agregar favorito: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", e);
        }
    }

    @DeleteMapping("/isla/{islaId}/favorito")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> quitarFavorito(@PathVariable Long islaId) {
        try {
            Usuario usuario = getUsuarioAutenticado();

            // Buscar el favorito existente
            Favorito favorito = favoritoRepository.findByUsuarioIdAndIslaId(usuario.getId(), islaId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Esta isla no está en tus favoritos"));

            // Eliminar el favorito
            favoritoRepository.delete(favorito);

            logger.info("Favorito eliminado exitosamente para usuario {} en isla {}", usuario.getId(), islaId);
            return ResponseEntity.ok("Favorito eliminado exitosamente");

        } catch (ResponseStatusException e) {
            logger.error("Error al quitar favorito: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error interno al quitar favorito: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", e);
        }
    }

    @GetMapping("/isla/{islaId}/like/status")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> verificarMeGusta(@PathVariable Long islaId) {
        try {
            Usuario usuario = getUsuarioAutenticado();
            boolean existe = meGustaRepository.existsByUsuarioIdAndIslaId(usuario.getId(), islaId);
            return ResponseEntity.ok(existe);
        } catch (Exception e) {
            logger.error("Error al verificar me gusta: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", e);
        }
    }

    @GetMapping("/isla/{islaId}/favorito/status")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> verificarFavorito(@PathVariable Long islaId) {
        try {
            Usuario usuario = getUsuarioAutenticado();
            boolean existe = favoritoRepository.existsByUsuarioIdAndIslaId(usuario.getId(), islaId);
            return ResponseEntity.ok(existe);
        } catch (Exception e) {
            logger.error("Error al verificar favorito: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", e);
        }
    }

    @GetMapping("/isla/{islaId}/puntuacion")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Integer> obtenerPuntuacionUsuario(@PathVariable Long islaId) {
        try {
            Usuario usuario = getUsuarioAutenticado();
            Puntuacion puntuacion = puntuacionRepository.findByUsuarioIdAndIslaId(usuario.getId(), islaId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No has puntuado esta isla"));
            return ResponseEntity.ok(puntuacion.getCalificacion());
        } catch (ResponseStatusException e) {
            logger.error("Error al obtener puntuación: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error interno al obtener puntuación: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", e);
        }
    }

    @PostMapping("/isla/{islaId}/puntuacion")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> establecerPuntuacion(@PathVariable Long islaId, @RequestParam int calificacion) {
        try {
            if (calificacion < 1 || calificacion > 5) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La calificación debe estar entre 1 y 5");
            }

            Usuario usuario = getUsuarioAutenticado();

            // Verificar que la isla existe
            Isla isla = islaRepository.findById(islaId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Isla no encontrada"));

            // Buscar puntuación existente
            Optional<Puntuacion> puntuacionExistente = puntuacionRepository.findByUsuarioIdAndIslaId(usuario.getId(), islaId);

            if (puntuacionExistente.isPresent()) {
                // Actualizar puntuación existente
                Puntuacion puntuacion = puntuacionExistente.get();
                puntuacion.setCalificacion(calificacion);
                puntuacionRepository.save(puntuacion);
                logger.info("Puntuación actualizada exitosamente para usuario {} en isla {}", usuario.getId(), islaId);
                return ResponseEntity.ok("Puntuación actualizada exitosamente");
            } else {
                // Crear nueva puntuación
                Puntuacion nuevaPuntuacion = new Puntuacion();
                nuevaPuntuacion.setUsuario(usuario);
                nuevaPuntuacion.setIsla(isla);
                nuevaPuntuacion.setCalificacion(calificacion);
                puntuacionRepository.save(nuevaPuntuacion);
                logger.info("Puntuación creada exitosamente para usuario {} en isla {}", usuario.getId(), islaId);
                return ResponseEntity.ok("Puntuación creada exitosamente");
            }

        } catch (ResponseStatusException e) {
            logger.error("Error al establecer puntuación: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error interno al establecer puntuación: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", e);
        }
    }

    @GetMapping("/isla/{islaId}/puntuacion/promedio")
    public ResponseEntity<Double> obtenerPromedioPuntuaciones(@PathVariable Long islaId) {
        try {
            // Verificar que la isla existe
            islaRepository.findById(islaId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Isla no encontrada"));

            Optional<Double> promedio = puntuacionRepository.findAverageCalificacionByIslaId(islaId);
            if (promedio.isPresent()) {
                return ResponseEntity.ok(promedio.get());
            } else {
                return ResponseEntity.ok(0.0); // Si no hay puntuaciones, devolver 0
            }
        } catch (ResponseStatusException e) {
            logger.error("Error al obtener promedio de puntuaciones: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error interno al obtener promedio de puntuaciones: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", e);
        }
    }
}