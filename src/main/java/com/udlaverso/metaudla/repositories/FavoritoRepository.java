package com.udlaverso.metaudla.repositories;

import com.udlaverso.metaudla.models.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoritoRepository extends JpaRepository<Favorito, Long> {
}