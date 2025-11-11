package com.udlaverso.metaudla.repositories;

import com.udlaverso.metaudla.entities.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoritoRepository extends JpaRepository<Favorito, Long> {
    Optional<Favorito> findByUsuarioIdAndIslaId(Long usuarioId, Long islaId);
    boolean existsByUsuarioIdAndIslaId(Long usuarioId, Long islaId);
}