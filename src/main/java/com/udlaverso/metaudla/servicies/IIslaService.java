package com.udlaverso.metaudla.servicies;

import com.udlaverso.metaudla.enums.EstadoBasico;
import com.udlaverso.metaudla.enums.TipoLike;
import com.udlaverso.metaudla.models.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IIslaService {

    // CRUD Básico
    Isla crearIsla(Isla isla, Usuario creador);
    Isla actualizarIsla(Long id, Isla islaActualizada, Usuario actualizador);
    Optional<Isla> buscarPorId(Long id);
    Optional<Isla> buscarPorIdConEstadisticas(Long id);
    List<Isla> listarTodas();
    Page<Isla> listarConPaginacion(Pageable pageable);

    // Búsquedas
    List<Isla> buscarPorNombre(String nombre);
    List<Isla> buscarPorEtiqueta(String etiqueta);
    List<Isla> buscarPorAutor(String autor);
    List<Isla> listarPorEstado(EstadoBasico estado);

    // Gestión de Estadísticas
    Isla actualizarEstadisticas(Long islaId);
    void actualizarEstadisticasGlobales();

    // Interacciones
    void incrementarVisitas(Long islaId);
    void agregarMeGusta(Long islaId, Long usuarioId, TipoLike tipo);
    void agregarComentario(Long islaId, String contenido, Usuario autor);
    void agregarFavorito(Long islaId, Usuario usuario);
    void agregarPuntuacion(Long islaId, int calificacion, Usuario usuario);

    // Rankings y Estadísticas
    List<Isla> obtenerMasPopulares(int limite);
    List<Isla> obtenerMejorPuntuadas(int limite);
    List<Isla> obtenerMasComentadas(int limite);
    List<Isla> obtenerMasFavoritas(int limite);
    List<Isla> obtenerMasRecientes(int limite);
    List<Isla> obtenerMasVisitadas(int limite);

    // Estadísticas Globales
    long contarIslasActivas();
    long contarTotalVisitas();
    long contarTotalInteracciones();
}