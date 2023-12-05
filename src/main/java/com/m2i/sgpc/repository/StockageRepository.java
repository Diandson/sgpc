package com.m2i.sgpc.repository;

import com.m2i.sgpc.domain.Stockage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Stockage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StockageRepository extends JpaRepository<Stockage, Long> {}
