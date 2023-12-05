package com.m2i.sgpc.repository;

import com.m2i.sgpc.domain.Production;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Production entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductionRepository extends JpaRepository<Production, Long> {}
