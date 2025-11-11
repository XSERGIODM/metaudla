package com.udlaverso.metaudla.repositories;

import com.udlaverso.metaudla.entities.Puntuacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PuntuacionRepository extends JpaRepository<Puntuacion, Long> {

    Optional<Puntuacion> findByUsuarioIdAndIslaId(Long usuarioId, Long islaId);

    boolean existsByUsuarioIdAndIslaId(Long usuarioId, Long islaId);

    @Query("SELECT AVG(p.calificacion) FROM puntuacion p WHERE p.isla.id = :islaId")
    Optional<Double> findAverageCalificacionByIslaId(@Param("islaId") Long islaId);
}