package com.udlaverso.metaudla.repositories;

import com.udlaverso.metaudla.entities.Isla;
import com.udlaverso.metaudla.enums.TipoLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IslaRepository extends JpaRepository<Isla, Long> {

    @Query("SELECT i FROM isla i LEFT JOIN i.meGustas m WHERE m.tipo = :tipo GROUP BY i ORDER BY COUNT(m) DESC")
    Page<Isla> findAllSortedByMeGustasCount(@Param("tipo") TipoLike tipo, Pageable pageable);
}