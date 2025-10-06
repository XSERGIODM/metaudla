package com.udlaverso.metaudla.repositories;

import com.udlaverso.metaudla.entities.LikeComentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeComentarioRepository extends JpaRepository<LikeComentario, Long> {
}