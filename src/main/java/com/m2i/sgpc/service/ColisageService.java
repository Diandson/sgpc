package com.m2i.sgpc.service;

import com.m2i.sgpc.domain.Colisage;
import com.m2i.sgpc.repository.ColisageRepository;
import com.m2i.sgpc.service.dto.ColisageDTO;
import com.m2i.sgpc.service.mapper.ColisageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.m2i.sgpc.domain.Colisage}.
 */
@Service
@Transactional
public class ColisageService {

    private final Logger log = LoggerFactory.getLogger(ColisageService.class);

    private final ColisageRepository colisageRepository;

    private final ColisageMapper colisageMapper;

    public ColisageService(ColisageRepository colisageRepository, ColisageMapper colisageMapper) {
        this.colisageRepository = colisageRepository;
        this.colisageMapper = colisageMapper;
    }

    /**
     * Save a colisage.
     *
     * @param colisageDTO the entity to save.
     * @return the persisted entity.
     */
    public ColisageDTO save(ColisageDTO colisageDTO) {
        log.debug("Request to save Colisage : {}", colisageDTO);
        Colisage colisage = colisageMapper.toEntity(colisageDTO);
        colisage = colisageRepository.save(colisage);
        return colisageMapper.toDto(colisage);
    }

    /**
     * Update a colisage.
     *
     * @param colisageDTO the entity to save.
     * @return the persisted entity.
     */
    public ColisageDTO update(ColisageDTO colisageDTO) {
        log.debug("Request to update Colisage : {}", colisageDTO);
        Colisage colisage = colisageMapper.toEntity(colisageDTO);
        colisage = colisageRepository.save(colisage);
        return colisageMapper.toDto(colisage);
    }

    /**
     * Partially update a colisage.
     *
     * @param colisageDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ColisageDTO> partialUpdate(ColisageDTO colisageDTO) {
        log.debug("Request to partially update Colisage : {}", colisageDTO);

        return colisageRepository
            .findById(colisageDTO.getId())
            .map(existingColisage -> {
                colisageMapper.partialUpdate(existingColisage, colisageDTO);

                return existingColisage;
            })
            .map(colisageRepository::save)
            .map(colisageMapper::toDto);
    }

    /**
     * Get all the colisages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ColisageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Colisages");
        return colisageRepository.findAll(pageable).map(colisageMapper::toDto);
    }

    /**
     * Get one colisage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ColisageDTO> findOne(Long id) {
        log.debug("Request to get Colisage : {}", id);
        return colisageRepository.findById(id).map(colisageMapper::toDto);
    }

    /**
     * Delete the colisage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Colisage : {}", id);
        colisageRepository.deleteById(id);
    }
}
