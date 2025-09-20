package com.udlaverso.metaudla.repositories;

import com.udlaverso.metaudla.models.MeGusta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeGustaRepository extends JpaRepository<MeGusta, Long> {
}