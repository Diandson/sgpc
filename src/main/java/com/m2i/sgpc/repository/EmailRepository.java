package com.m2i.sgpc.repository;

import com.m2i.sgpc.domain.Email;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Email entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {}
