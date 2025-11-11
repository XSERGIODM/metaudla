package com.udlaverso.metaudla.repositories;

import com.udlaverso.metaudla.entities.LikeComentario;
import com.udlaverso.metaudla.enums.TipoLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeComentarioRepository extends JpaRepository<LikeComentario, Long> {

    // Buscar like por usuario y comentario
    Optional<LikeComentario> findByUsuarioIdAndComentarioId(Long usuarioId, Long comentarioId);

    // Contar likes por comentario y tipo
    long countByComentarioIdAndTipo(Long comentarioId, TipoLike tipo);

    // Verificar si existe like de usuario en comentario
    boolean existsByUsuarioIdAndComentarioId(Long usuarioId, Long comentarioId);

    // Eliminar like por usuario y comentario
    void deleteByUsuarioIdAndComentarioId(Long usuarioId, Long comentarioId);

    // Buscar likes por comentario
    @Query("SELECT l FROM like_comentario l WHERE l.comentario.id = :comentarioId ORDER BY l.fechaCreacion DESC")
    java.util.List<LikeComentario> findByComentarioIdOrderByFechaCreacionDesc(@Param("comentarioId") Long comentarioId);
}