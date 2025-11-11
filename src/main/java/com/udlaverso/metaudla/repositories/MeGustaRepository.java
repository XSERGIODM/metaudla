package com.udlaverso.metaudla.repositories;

import com.udlaverso.metaudla.entities.MeGusta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MeGustaRepository extends JpaRepository<MeGusta, Long> {
    Optional<MeGusta> findByUsuarioIdAndIslaId(Long usuarioId, Long islaId);
    boolean existsByUsuarioIdAndIslaId(Long usuarioId, Long islaId);
}