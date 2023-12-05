package com.m2i.sgpc.service;

import com.m2i.sgpc.domain.Production;
import com.m2i.sgpc.repository.ProductionRepository;
import com.m2i.sgpc.service.dto.ProductionDTO;
import com.m2i.sgpc.service.mapper.ProductionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.m2i.sgpc.domain.Production}.
 */
@Service
@Transactional
public class ProductionService {

    private final Logger log = LoggerFactory.getLogger(ProductionService.class);

    private final ProductionRepository productionRepository;

    private final ProductionMapper productionMapper;

    public ProductionService(ProductionRepository productionRepository, ProductionMapper productionMapper) {
        this.productionRepository = productionRepository;
        this.productionMapper = productionMapper;
    }

    /**
     * Save a production.
     *
     * @param productionDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductionDTO save(ProductionDTO productionDTO) {
        log.debug("Request to save Production : {}", productionDTO);
        Production production = productionMapper.toEntity(productionDTO);
        production = productionRepository.save(production);
        return productionMapper.toDto(production);
    }

    /**
     * Update a production.
     *
     * @param productionDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductionDTO update(ProductionDTO productionDTO) {
        log.debug("Request to update Production : {}", productionDTO);
        Production production = productionMapper.toEntity(productionDTO);
        production = productionRepository.save(production);
        return productionMapper.toDto(production);
    }

    /**
     * Partially update a production.
     *
     * @param productionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductionDTO> partialUpdate(ProductionDTO productionDTO) {
        log.debug("Request to partially update Production : {}", productionDTO);

        return productionRepository
            .findById(productionDTO.getId())
            .map(existingProduction -> {
                productionMapper.partialUpdate(existingProduction, productionDTO);

                return existingProduction;
            })
            .map(productionRepository::save)
            .map(productionMapper::toDto);
    }

    /**
     * Get all the productions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Productions");
        return productionRepository.findAll(pageable).map(productionMapper::toDto);
    }

    /**
     * Get one production by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductionDTO> findOne(Long id) {
        log.debug("Request to get Production : {}", id);
        return productionRepository.findById(id).map(productionMapper::toDto);
    }

    /**
     * Delete the production by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Production : {}", id);
        productionRepository.deleteById(id);
    }
}
