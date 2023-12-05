package com.m2i.sgpc.service;

import com.m2i.sgpc.domain.Personne;
import com.m2i.sgpc.repository.PersonneRepository;
import com.m2i.sgpc.service.dto.PersonneDTO;
import com.m2i.sgpc.service.mapper.PersonneMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.m2i.sgpc.domain.Personne}.
 */
@Service
@Transactional
public class PersonneService {

    private final Logger log = LoggerFactory.getLogger(PersonneService.class);

    private final PersonneRepository personneRepository;

    private final PersonneMapper personneMapper;

    public PersonneService(PersonneRepository personneRepository, PersonneMapper personneMapper) {
        this.personneRepository = personneRepository;
        this.personneMapper = personneMapper;
    }

    /**
     * Save a personne.
     *
     * @param personneDTO the entity to save.
     * @return the persisted entity.
     */
    public PersonneDTO save(PersonneDTO personneDTO) {
        log.debug("Request to save Personne : {}", personneDTO);
        Personne personne = personneMapper.toEntity(personneDTO);
        personne = personneRepository.save(personne);
        return personneMapper.toDto(personne);
    }

    /**
     * Update a personne.
     *
     * @param personneDTO the entity to save.
     * @return the persisted entity.
     */
    public PersonneDTO update(PersonneDTO personneDTO) {
        log.debug("Request to update Personne : {}", personneDTO);
        Personne personne = personneMapper.toEntity(personneDTO);
        personne = personneRepository.save(personne);
        return personneMapper.toDto(personne);
    }

    /**
     * Partially update a personne.
     *
     * @param personneDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PersonneDTO> partialUpdate(PersonneDTO personneDTO) {
        log.debug("Request to partially update Personne : {}", personneDTO);

        return personneRepository
            .findById(personneDTO.getId())
            .map(existingPersonne -> {
                personneMapper.partialUpdate(existingPersonne, personneDTO);

                return existingPersonne;
            })
            .map(personneRepository::save)
            .map(personneMapper::toDto);
    }

    /**
     * Get all the personnes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PersonneDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Personnes");
        return personneRepository.findAll(pageable).map(personneMapper::toDto);
    }

    /**
     * Get all the personnes with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PersonneDTO> findAllWithEagerRelationships(Pageable pageable) {
        return personneRepository.findAllWithEagerRelationships(pageable).map(personneMapper::toDto);
    }

    /**
     * Get one personne by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PersonneDTO> findOne(Long id) {
        log.debug("Request to get Personne : {}", id);
        return personneRepository.findOneWithEagerRelationships(id).map(personneMapper::toDto);
    }

    /**
     * Delete the personne by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Personne : {}", id);
        personneRepository.deleteById(id);
    }
}
