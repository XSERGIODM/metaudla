package com.udlaverso.metaudla.servicies.implementaciones;

import com.udlaverso.metaudla.enums.EstadoBasico;
import com.udlaverso.metaudla.enums.TipoLike;
import com.udlaverso.metaudla.models.*;
import com.udlaverso.metaudla.repositories.IslaRepository;
import com.udlaverso.metaudla.servicies.IIslaService;
import com.udlaverso.metaudla.servicies.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@EnableScheduling
public class IslaService implements IIslaService {

    @Autowired
    private IslaRepository islaRepository;

    @Autowired
    private IUsuarioService usuarioService;

    // Crear isla
    public Isla crearIsla(Isla isla, Usuario creador) {
        validarIsla(isla);

        // Establecer valores por defecto
        isla.setEstado(EstadoBasico.HABILITADO);
        isla.setVisitas(0);
        isla.setTotalMeGustas(0);
        isla.setTotalNoMeGustas(0);
        isla.setTotalComentarios(0);
        isla.setTotalFavoritos(0);
        isla.setPromedioPuntuacion(BigDecimal.ZERO);

        return islaRepository.save(isla);
    }

    // Actualizar isla
    public Isla actualizarIsla(Long id, Isla islaActualizada, Usuario actualizador) {
        Isla isla = islaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Isla no encontrada"));

        // Actualizar campos básicos
        isla.setNombre(islaActualizada.getNombre());
        isla.setDescripcion(islaActualizada.getDescripcion());
        isla.setImagenes(islaActualizada.getImagenes());
        isla.setVideos(islaActualizada.getVideos());
        isla.setLinkDescarga(islaActualizada.getLinkDescarga());
        isla.setAutor(islaActualizada.getAutor());
        isla.setEtiquetas(islaActualizada.getEtiquetas());
        isla.setEstado(islaActualizada.getEstado());

        // Auditoría
        isla.setFechaActualizacion(LocalDateTime.now());

        return islaRepository.save(isla);
    }

    // Buscar por ID con estadísticas
    public Optional<Isla> buscarPorId(Long id) {
        return islaRepository.findById(id);
    }

    public Optional<Isla> buscarPorIdConEstadisticas(Long id) {
        return islaRepository.findByIdWithStats(id);
    }

    // Listar todas las islas
    public List<Isla> listarTodas() {
        return islaRepository.findAll();
    }

    // Listar con paginación
    public Page<Isla> listarConPaginacion(Pageable pageable) {
        return islaRepository.findAll(pageable);
    }

    // Buscar por nombre
    public List<Isla> buscarPorNombre(String nombre) {
        return islaRepository.findByNombreContainingIgnoreCase(nombre);
    }

    // Buscar por etiqueta
    public List<Isla> buscarPorEtiqueta(String etiqueta) {
        return islaRepository.findByEtiquetasContaining(etiqueta);
    }

    // Buscar por autor
    public List<Isla> buscarPorAutor(String autor) {
        return islaRepository.findByAutor(autor);
    }

    // Listar por estado
    public List<Isla> listarPorEstado(EstadoBasico estado) {
        return islaRepository.findByEstado(estado);
    }

    // Incrementar visitas
    public void incrementarVisitas(Long islaId) {
        Isla isla = islaRepository.findById(islaId)
            .orElseThrow(() -> new RuntimeException("Isla no encontrada"));

        isla.setVisitas(isla.getVisitas() + 1);
        islaRepository.save(isla);
    }

    // Gestión de estadísticas
    public Isla actualizarEstadisticas(Long islaId) {
        Isla isla = islaRepository.findByIdWithStats(islaId)
            .orElseThrow(() -> new RuntimeException("Isla no encontrada"));

        isla.recalcularEstadisticas();
        return islaRepository.save(isla);
    }

    // Actualización automática de estadísticas (cada 5 minutos)
    @Scheduled(fixedRate = 300000)
    public void actualizarEstadisticasGlobales() {
        islaRepository.actualizarEstadisticasGlobales();
    }

    // Gestión de Me Gusta
    public void agregarMeGusta(Long islaId, Long usuarioId, TipoLike tipo) {
        Isla isla = islaRepository.findById(islaId)
            .orElseThrow(() -> new RuntimeException("Isla no encontrada"));

        Usuario usuario = usuarioService.buscarPorId(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Aquí iría la lógica para crear/actualizar MeGusta
        // Por ahora solo actualizamos las estadísticas
        actualizarEstadisticas(islaId);
    }

    // Gestión de comentarios
    public void agregarComentario(Long islaId, String contenido, Usuario autor) {
        Isla isla = islaRepository.findById(islaId)
            .orElseThrow(() -> new RuntimeException("Isla no encontrada"));

        // Aquí iría la lógica para crear comentario
        // Por ahora solo actualizamos las estadísticas
        actualizarEstadisticas(islaId);
    }

    // Gestión de favoritos
    public void agregarFavorito(Long islaId, Usuario usuario) {
        Isla isla = islaRepository.findById(islaId)
            .orElseThrow(() -> new RuntimeException("Isla no encontrada"));

        // Aquí iría la lógica para crear favorito
        // Por ahora solo actualizamos las estadísticas
        actualizarEstadisticas(islaId);
    }

    // Gestión de puntuaciones
    public void agregarPuntuacion(Long islaId, int calificacion, Usuario usuario) {
        Isla isla = islaRepository.findById(islaId)
            .orElseThrow(() -> new RuntimeException("Isla no encontrada"));

        // Aquí iría la lógica para crear puntuación
        // Actualizar promedio
        isla.recalcularPromedio(isla.getPuntuaciones());
        islaRepository.save(isla);
    }

    // Estadísticas y reportes
    public List<Isla> obtenerMasPopulares(int limite) {
        Pageable pageable = PageRequest.of(0, limite);
        return islaRepository.findTopByTotalInteracciones(pageable);
    }

    public List<Isla> obtenerMejorPuntuadas(int limite) {
        Pageable pageable = PageRequest.of(0, limite);
        return islaRepository.findTopByPromedioPuntuacion(pageable);
    }

    public List<Isla> obtenerMasComentadas(int limite) {
        Pageable pageable = PageRequest.of(0, limite);
        return islaRepository.findTopByComentarios(pageable);
    }

    public List<Isla> obtenerMasFavoritas(int limite) {
        Pageable pageable = PageRequest.of(0, limite);
        return islaRepository.findTopByFavoritos(pageable);
    }

    public List<Isla> obtenerMasRecientes(int limite) {
        Pageable pageable = PageRequest.of(0, limite);
        return islaRepository.findMasRecientes(pageable);
    }

    public List<Isla> obtenerMasVisitadas(int limite) {
        Pageable pageable = PageRequest.of(0, limite);
        return islaRepository.findMasVisitadas(pageable);
    }

    // Estadísticas generales
    public long contarIslasActivas() {
        return islaRepository.countByEstado(EstadoBasico.HABILITADO);
    }

    public long contarTotalVisitas() {
        return islaRepository.sumAllVisitas();
    }

    public long contarTotalInteracciones() {
        return islaRepository.sumAllInteracciones();
    }

    // Validaciones
    private void validarIsla(Isla isla) {
        if (isla.getNombre() == null || isla.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la isla es obligatorio");
        }

        if (isla.getAutor() == null || isla.getAutor().trim().isEmpty()) {
            throw new IllegalArgumentException("El autor es obligatorio");
        }

        // Validar URLs si están presentes
        if (isla.getImagenes() != null) {
            for (String imagen : isla.getImagenes()) {
                if (imagen != null && !imagen.matches("^https?://.*")) {
                    throw new IllegalArgumentException("Las URLs de imágenes deben comenzar con http:// o https://");
                }
            }
        }

        if (isla.getVideos() != null) {
            for (String video : isla.getVideos()) {
                if (video != null && !video.matches("^https?://.*")) {
                    throw new IllegalArgumentException("Las URLs de videos deben comenzar con http:// o https://");
                }
            }
        }

        if (isla.getLinkDescarga() != null && !isla.getLinkDescarga().matches("^https?://.*")) {
            throw new IllegalArgumentException("La URL de descarga debe comenzar con http:// o https://");
        }
    }
}