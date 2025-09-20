package com.udlaverso.metaudla.repositories;

import com.udlaverso.metaudla.models.Isla;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IslaRepository extends JpaRepository<Isla, Long> {
}