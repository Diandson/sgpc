package com.m2i.sgpc.service;

import com.m2i.sgpc.domain.Filiale;
import com.m2i.sgpc.repository.FilialeRepository;
import com.m2i.sgpc.service.dto.FilialeDTO;
import com.m2i.sgpc.service.mapper.FilialeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.m2i.sgpc.domain.Filiale}.
 */
@Service
@Transactional
public class FilialeService {

    private final Logger log = LoggerFactory.getLogger(FilialeService.class);

    private final FilialeRepository filialeRepository;

    private final FilialeMapper filialeMapper;

    public FilialeService(FilialeRepository filialeRepository, FilialeMapper filialeMapper) {
        this.filialeRepository = filialeRepository;
        this.filialeMapper = filialeMapper;
    }

    /**
     * Save a filiale.
     *
     * @param filialeDTO the entity to save.
     * @return the persisted entity.
     */
    public FilialeDTO save(FilialeDTO filialeDTO) {
        log.debug("Request to save Filiale : {}", filialeDTO);
        Filiale filiale = filialeMapper.toEntity(filialeDTO);
        filiale = filialeRepository.save(filiale);
        return filialeMapper.toDto(filiale);
    }

    /**
     * Update a filiale.
     *
     * @param filialeDTO the entity to save.
     * @return the persisted entity.
     */
    public FilialeDTO update(FilialeDTO filialeDTO) {
        log.debug("Request to update Filiale : {}", filialeDTO);
        Filiale filiale = filialeMapper.toEntity(filialeDTO);
        filiale = filialeRepository.save(filiale);
        return filialeMapper.toDto(filiale);
    }

    /**
     * Partially update a filiale.
     *
     * @param filialeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FilialeDTO> partialUpdate(FilialeDTO filialeDTO) {
        log.debug("Request to partially update Filiale : {}", filialeDTO);

        return filialeRepository
            .findById(filialeDTO.getId())
            .map(existingFiliale -> {
                filialeMapper.partialUpdate(existingFiliale, filialeDTO);

                return existingFiliale;
            })
            .map(filialeRepository::save)
            .map(filialeMapper::toDto);
    }

    /**
     * Get all the filiales.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FilialeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Filiales");
        return filialeRepository.findAll(pageable).map(filialeMapper::toDto);
    }

    /**
     * Get one filiale by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FilialeDTO> findOne(Long id) {
        log.debug("Request to get Filiale : {}", id);
        return filialeRepository.findById(id).map(filialeMapper::toDto);
    }

    /**
     * Delete the filiale by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Filiale : {}", id);
        filialeRepository.deleteById(id);
    }
}
