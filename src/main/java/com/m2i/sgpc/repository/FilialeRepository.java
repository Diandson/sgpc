package com.m2i.sgpc.repository;

import com.m2i.sgpc.domain.Filiale;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Filiale entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FilialeRepository extends JpaRepository<Filiale, Long> {}
