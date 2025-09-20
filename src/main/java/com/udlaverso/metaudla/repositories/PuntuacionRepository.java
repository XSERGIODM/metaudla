package com.udlaverso.metaudla.repositories;

import com.udlaverso.metaudla.models.Puntuacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PuntuacionRepository extends JpaRepository<Puntuacion, Long> {
}