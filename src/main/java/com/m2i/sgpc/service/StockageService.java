package com.m2i.sgpc.service;

import com.m2i.sgpc.domain.Stockage;
import com.m2i.sgpc.repository.StockageRepository;
import com.m2i.sgpc.service.dto.StockageDTO;
import com.m2i.sgpc.service.mapper.StockageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.m2i.sgpc.domain.Stockage}.
 */
@Service
@Transactional
public class StockageService {

    private final Logger log = LoggerFactory.getLogger(StockageService.class);

    private final StockageRepository stockageRepository;

    private final StockageMapper stockageMapper;

    public StockageService(StockageRepository stockageRepository, StockageMapper stockageMapper) {
        this.stockageRepository = stockageRepository;
        this.stockageMapper = stockageMapper;
    }

    /**
     * Save a stockage.
     *
     * @param stockageDTO the entity to save.
     * @return the persisted entity.
     */
    public StockageDTO save(StockageDTO stockageDTO) {
        log.debug("Request to save Stockage : {}", stockageDTO);
        Stockage stockage = stockageMapper.toEntity(stockageDTO);
        stockage = stockageRepository.save(stockage);
        return stockageMapper.toDto(stockage);
    }

    /**
     * Update a stockage.
     *
     * @param stockageDTO the entity to save.
     * @return the persisted entity.
     */
    public StockageDTO update(StockageDTO stockageDTO) {
        log.debug("Request to update Stockage : {}", stockageDTO);
        Stockage stockage = stockageMapper.toEntity(stockageDTO);
        stockage = stockageRepository.save(stockage);
        return stockageMapper.toDto(stockage);
    }

    /**
     * Partially update a stockage.
     *
     * @param stockageDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<StockageDTO> partialUpdate(StockageDTO stockageDTO) {
        log.debug("Request to partially update Stockage : {}", stockageDTO);

        return stockageRepository
            .findById(stockageDTO.getId())
            .map(existingStockage -> {
                stockageMapper.partialUpdate(existingStockage, stockageDTO);

                return existingStockage;
            })
            .map(stockageRepository::save)
            .map(stockageMapper::toDto);
    }

    /**
     * Get all the stockages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<StockageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Stockages");
        return stockageRepository.findAll(pageable).map(stockageMapper::toDto);
    }

    /**
     * Get one stockage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StockageDTO> findOne(Long id) {
        log.debug("Request to get Stockage : {}", id);
        return stockageRepository.findById(id).map(stockageMapper::toDto);
    }

    /**
     * Delete the stockage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Stockage : {}", id);
        stockageRepository.deleteById(id);
    }
}
