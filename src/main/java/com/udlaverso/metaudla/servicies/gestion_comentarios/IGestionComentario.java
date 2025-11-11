package com.udlaverso.metaudla.servicies.gestion_comentarios;

import com.udlaverso.metaudla.DTOs.ComentarioDtoCreate;
import com.udlaverso.metaudla.DTOs.ComentarioDtoResponse;
import com.udlaverso.metaudla.entities.Comentario;
import com.udlaverso.metaudla.enums.EstadoModeracion;
import com.udlaverso.metaudla.enums.TipoLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IGestionComentario {

    // CRUD básico de comentarios
    ComentarioDtoResponse crearComentario(ComentarioDtoCreate comentarioDto, Long usuarioId);
    ComentarioDtoResponse actualizarComentario(Long comentarioId, ComentarioDtoCreate comentarioDto, Long usuarioId);
    void eliminarComentario(Long comentarioId, Long usuarioId);
    Optional<ComentarioDtoResponse> obtenerComentarioPorId(Long comentarioId);

    // Listado de comentarios
    List<ComentarioDtoResponse> obtenerComentariosPorIsla(Long islaId);
    Page<ComentarioDtoResponse> obtenerComentariosPorIslaPaginados(Long islaId, Pageable pageable);
    List<ComentarioDtoResponse> obtenerComentariosPorUsuario(Long usuarioId);

    // Sistema de likes
    void agregarLikeComentario(Long comentarioId, Long usuarioId, TipoLike tipo);
    void quitarLikeComentario(Long comentarioId, Long usuarioId);
    boolean verificarLikeUsuario(Long comentarioId, Long usuarioId);
    String obtenerTipoLikeUsuario(Long comentarioId, Long usuarioId);

    // Moderación
    void moderarComentario(Long comentarioId, EstadoModeracion estado, Long moderadorId);
    List<ComentarioDtoResponse> obtenerComentariosPendientesModeracion();

    // Estadísticas
    long contarComentariosPorIsla(Long islaId);
    int contarLikesComentario(Long comentarioId, TipoLike tipo);

    // Respuestas anidadas
    ComentarioDtoResponse crearRespuesta(Long comentarioPadreId, ComentarioDtoCreate respuestaDto, Long usuarioId);
    List<ComentarioDtoResponse> obtenerRespuestasComentario(Long comentarioPadreId);
}