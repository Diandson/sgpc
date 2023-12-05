package com.m2i.sgpc.repository;

import com.m2i.sgpc.domain.Colisage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Colisage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ColisageRepository extends JpaRepository<Colisage, Long> {}
