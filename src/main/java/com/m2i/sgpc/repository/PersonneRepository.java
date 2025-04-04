package com.m2i.sgpc.repository;

import com.m2i.sgpc.config.Constants;
import com.m2i.sgpc.domain.Personne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Personne entity.
 */
@Repository
public interface PersonneRepository extends JpaRepository<Personne, Long> {
    default Optional<Personne> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Personne> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Personne> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select personne from Personne personne left join fetch personne.user",
        countQuery = "select count(personne) from Personne personne"
    )
    Page<Personne> findAllWithToOneRelationships(Pageable pageable);

    @Query("select personne from Personne personne left join fetch personne.user")
    List<Personne> findAllWithToOneRelationships();

    @Query("select personne from Personne personne left join fetch personne.user where personne.id =:id")
    Optional<Personne> findOneWithToOneRelationships(@Param("id") Long id);

    Boolean existsByUserId(Long user_id);

    Personne getByUserId(Long user_id);
    Personne findByUserLogin(@NotNull @Pattern(regexp = Constants.LOGIN_REGEX) @Size(min = 1, max = 50) String user_login);
}
