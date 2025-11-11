package com.udlaverso.metaudla.repositories;

import com.udlaverso.metaudla.entities.Comentario;
import com.udlaverso.metaudla.enums.EstadoModeracion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    // Buscar comentarios por isla
    List<Comentario> findByIslaIdAndComentarioPadreIsNullOrderByFechaCreacionDesc(Long islaId);

    // Buscar comentarios por isla con paginación
    Page<Comentario> findByIslaIdAndComentarioPadreIsNullAndEstadoModeracionOrderByFechaCreacionDesc(
            Long islaId, EstadoModeracion estadoModeracion, Pageable pageable);

    // Buscar respuestas de un comentario padre
    List<Comentario> findByComentarioPadreIdOrderByFechaCreacionAsc(Long comentarioPadreId);

    // Buscar comentario por ID con respuestas
    @Query("SELECT c FROM comentario c LEFT JOIN FETCH c.respuestas WHERE c.id = :id")
    Optional<Comentario> findByIdWithRespuestas(@Param("id") Long id);

    // Buscar comentarios por usuario
    List<Comentario> findByUsuarioIdOrderByFechaCreacionDesc(Long usuarioId);

    // Contar comentarios por isla
    long countByIslaId(Long islaId);

    // Buscar comentarios pendientes de moderación
    List<Comentario> findByEstadoModeracionOrderByFechaCreacionAsc(EstadoModeracion estadoModeracion);
}