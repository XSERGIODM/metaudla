package com.udlaverso.metaudla.servicies.gestion_comentarios;

import com.udlaverso.metaudla.DTOs.ComentarioDtoCreate;
import com.udlaverso.metaudla.DTOs.ComentarioDtoResponse;
import com.udlaverso.metaudla.entities.Comentario;
import com.udlaverso.metaudla.entities.Isla;
import com.udlaverso.metaudla.entities.LikeComentario;
import com.udlaverso.metaudla.entities.Usuario;
import com.udlaverso.metaudla.enums.EstadoModeracion;
import com.udlaverso.metaudla.enums.TipoLike;
import com.udlaverso.metaudla.repositories.ComentarioRepository;
import com.udlaverso.metaudla.repositories.IslaRepository;
import com.udlaverso.metaudla.repositories.LikeComentarioRepository;
import com.udlaverso.metaudla.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GestionComentarioImpl implements IGestionComentario {

    private final ComentarioRepository comentarioRepository;
    private final LikeComentarioRepository likeComentarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final IslaRepository islaRepository;

    @Override
    public ComentarioDtoResponse crearComentario(ComentarioDtoCreate comentarioDto, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Isla isla = islaRepository.findById(comentarioDto.getIslaId())
                .orElseThrow(() -> new RuntimeException("Isla no encontrada"));

        Comentario comentario = new Comentario();
        comentario.setContenido(comentarioDto.getContenido());
        comentario.setUsuario(usuario);
        comentario.setIsla(isla);

        if (comentarioDto.getComentarioPadreId() != null) {
            Comentario comentarioPadre = comentarioRepository.findById(comentarioDto.getComentarioPadreId())
                    .orElseThrow(() -> new RuntimeException("Comentario padre no encontrado"));
            comentario.setComentarioPadre(comentarioPadre);
        }

        Comentario savedComentario = comentarioRepository.save(comentario);
        return mapToDto(savedComentario, usuarioId);
    }

    @Override
    public ComentarioDtoResponse actualizarComentario(Long comentarioId, ComentarioDtoCreate comentarioDto, Long usuarioId) {
        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));

        // Verificar que el usuario sea el autor del comentario
        if (!comentario.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("No tienes permisos para editar este comentario");
        }

        comentario.setContenido(comentarioDto.getContenido());
        Comentario updatedComentario = comentarioRepository.save(comentario);
        return mapToDto(updatedComentario, usuarioId);
    }

    @Override
    public void eliminarComentario(Long comentarioId, Long usuarioId) {
        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));

        // Verificar que el usuario sea el autor del comentario
        if (!comentario.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("No tienes permisos para eliminar este comentario");
        }

        comentarioRepository.delete(comentario);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ComentarioDtoResponse> obtenerComentarioPorId(Long comentarioId) {
        return comentarioRepository.findById(comentarioId)
                .map(comentario -> mapToDto(comentario, null));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComentarioDtoResponse> obtenerComentariosPorIsla(Long islaId) {
        List<Comentario> comentarios = comentarioRepository
                .findByIslaIdAndComentarioPadreIsNullOrderByFechaCreacionDesc(islaId);

        return comentarios.stream()
                .map(comentario -> mapToDto(comentario, null))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ComentarioDtoResponse> obtenerComentariosPorIslaPaginados(Long islaId, Pageable pageable) {
        Page<Comentario> comentarios = comentarioRepository
                .findByIslaIdAndComentarioPadreIsNullAndEstadoModeracionOrderByFechaCreacionDesc(
                        islaId, EstadoModeracion.APROBADO, pageable);

        return comentarios.map(comentario -> mapToDto(comentario, null));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComentarioDtoResponse> obtenerComentariosPorUsuario(Long usuarioId) {
        List<Comentario> comentarios = comentarioRepository.findByUsuarioIdOrderByFechaCreacionDesc(usuarioId);
        return comentarios.stream()
                .map(comentario -> mapToDto(comentario, usuarioId))
                .collect(Collectors.toList());
    }

    @Override
    public void agregarLikeComentario(Long comentarioId, Long usuarioId, TipoLike tipo) {
        // Verificar si ya existe un like
        Optional<LikeComentario> existingLike = likeComentarioRepository
                .findByUsuarioIdAndComentarioId(usuarioId, comentarioId);

        if (existingLike.isPresent()) {
            LikeComentario like = existingLike.get();
            if (like.getTipo().equals(tipo)) {
                // Si es el mismo tipo, quitar el like
                likeComentarioRepository.delete(like);
            } else {
                // Si es diferente tipo, actualizar
                like.setTipo(tipo);
                likeComentarioRepository.save(like);
            }
        } else {
            // Crear nuevo like
            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            Comentario comentario = comentarioRepository.findById(comentarioId)
                    .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));

            LikeComentario like = new LikeComentario();
            like.setUsuario(usuario);
            like.setComentario(comentario);
            like.setTipo(tipo);
            likeComentarioRepository.save(like);
        }
    }

    @Override
    public void quitarLikeComentario(Long comentarioId, Long usuarioId) {
        likeComentarioRepository.deleteByUsuarioIdAndComentarioId(usuarioId, comentarioId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean verificarLikeUsuario(Long comentarioId, Long usuarioId) {
        return likeComentarioRepository.existsByUsuarioIdAndComentarioId(usuarioId, comentarioId);
    }

    @Override
    @Transactional(readOnly = true)
    public String obtenerTipoLikeUsuario(Long comentarioId, Long usuarioId) {
        Optional<LikeComentario> like = likeComentarioRepository
                .findByUsuarioIdAndComentarioId(usuarioId, comentarioId);
        return like.map(l -> l.getTipo().name()).orElse(null);
    }

    @Override
    public void moderarComentario(Long comentarioId, EstadoModeracion estado, Long moderadorId) {
        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));

        Usuario moderador = usuarioRepository.findById(moderadorId)
                .orElseThrow(() -> new RuntimeException("Moderador no encontrado"));

        comentario.setEstadoModeracion(estado);
        comentario.setModerador(moderador);
        comentario.setFechaModeracion(java.time.LocalDateTime.now());

        comentarioRepository.save(comentario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComentarioDtoResponse> obtenerComentariosPendientesModeracion() {
        List<Comentario> comentarios = comentarioRepository
                .findByEstadoModeracionOrderByFechaCreacionAsc(EstadoModeracion.PENDIENTE);
        return comentarios.stream()
                .map(comentario -> mapToDto(comentario, null))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long contarComentariosPorIsla(Long islaId) {
        return comentarioRepository.countByIslaId(islaId);
    }

    @Override
    @Transactional(readOnly = true)
    public int contarLikesComentario(Long comentarioId, TipoLike tipo) {
        return (int) likeComentarioRepository.countByComentarioIdAndTipo(comentarioId, tipo);
    }

    @Override
    public ComentarioDtoResponse crearRespuesta(Long comentarioPadreId, ComentarioDtoCreate respuestaDto, Long usuarioId) {
        respuestaDto.setComentarioPadreId(comentarioPadreId);
        return crearComentario(respuestaDto, usuarioId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComentarioDtoResponse> obtenerRespuestasComentario(Long comentarioPadreId) {
        List<Comentario> respuestas = comentarioRepository
                .findByComentarioPadreIdOrderByFechaCreacionAsc(comentarioPadreId);
        return respuestas.stream()
                .map(respuesta -> mapToDto(respuesta, null))
                .collect(Collectors.toList());
    }

    private ComentarioDtoResponse mapToDto(Comentario comentario, Long currentUserId) {
        ComentarioDtoResponse dto = new ComentarioDtoResponse();
        dto.setId(comentario.getId());
        dto.setContenido(comentario.getContenido());
        dto.setFechaCreacion(comentario.getFechaCreacion());
        dto.setEstadoModeracion(comentario.getEstadoModeracion());
        dto.setFechaModeracion(comentario.getFechaModeracion());

        // Usuario
        dto.setUsuarioId(comentario.getUsuario().getId());
        dto.setUsuarioNombre(comentario.getUsuario().getNombre());
        dto.setUsuarioUsername(comentario.getUsuario().getUsername());

        // Isla
        dto.setIslaId(comentario.getIsla().getId());
        dto.setIslaNombre(comentario.getIsla().getNombre());

        // Comentario padre
        if (comentario.getComentarioPadre() != null) {
            dto.setComentarioPadreId(comentario.getComentarioPadre().getId());
        }

        // Respuestas
        dto.setRespuestas(obtenerRespuestasComentario(comentario.getId()));

        // Likes
        dto.setLikesCount(contarLikesComentario(comentario.getId(), TipoLike.ME_GUSTA));
        dto.setDislikesCount(contarLikesComentario(comentario.getId(), TipoLike.NO_ME_GUSTA));

        // Like del usuario actual
        if (currentUserId != null) {
            dto.setUserLikeType(obtenerTipoLikeUsuario(comentario.getId(), currentUserId));
        }

        return dto;
    }
}