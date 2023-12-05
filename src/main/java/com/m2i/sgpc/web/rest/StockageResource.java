package com.m2i.sgpc.web.rest;

import com.m2i.sgpc.repository.StockageRepository;
import com.m2i.sgpc.service.StockageService;
import com.m2i.sgpc.service.dto.StockageDTO;
import com.m2i.sgpc.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.m2i.sgpc.domain.Stockage}.
 */
@RestController
@RequestMapping("/api/stockages")
public class StockageResource {

    private final Logger log = LoggerFactory.getLogger(StockageResource.class);

    private static final String ENTITY_NAME = "stockage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StockageService stockageService;

    private final StockageRepository stockageRepository;

    public StockageResource(StockageService stockageService, StockageRepository stockageRepository) {
        this.stockageService = stockageService;
        this.stockageRepository = stockageRepository;
    }

    /**
     * {@code POST  /stockages} : Create a new stockage.
     *
     * @param stockageDTO the stockageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new stockageDTO, or with status {@code 400 (Bad Request)} if the stockage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StockageDTO> createStockage(@RequestBody StockageDTO stockageDTO) throws URISyntaxException {
        log.debug("REST request to save Stockage : {}", stockageDTO);
        if (stockageDTO.getId() != null) {
            throw new BadRequestAlertException("A new stockage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StockageDTO result = stockageService.save(stockageDTO);
        return ResponseEntity
            .created(new URI("/api/stockages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /stockages/:id} : Updates an existing stockage.
     *
     * @param id the id of the stockageDTO to save.
     * @param stockageDTO the stockageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stockageDTO,
     * or with status {@code 400 (Bad Request)} if the stockageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the stockageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StockageDTO> updateStockage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StockageDTO stockageDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Stockage : {}, {}", id, stockageDTO);
        if (stockageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stockageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stockageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StockageDTO result = stockageService.update(stockageDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stockageDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /stockages/:id} : Partial updates given fields of an existing stockage, field will ignore if it is null
     *
     * @param id the id of the stockageDTO to save.
     * @param stockageDTO the stockageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stockageDTO,
     * or with status {@code 400 (Bad Request)} if the stockageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the stockageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the stockageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StockageDTO> partialUpdateStockage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StockageDTO stockageDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Stockage partially : {}, {}", id, stockageDTO);
        if (stockageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stockageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stockageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StockageDTO> result = stockageService.partialUpdate(stockageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stockageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /stockages} : get all the stockages.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stockages in body.
     */
    @GetMapping("")
    public ResponseEntity<List<StockageDTO>> getAllStockages(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Stockages");
        Page<StockageDTO> page = stockageService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /stockages/:id} : get the "id" stockage.
     *
     * @param id the id of the stockageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the stockageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StockageDTO> getStockage(@PathVariable Long id) {
        log.debug("REST request to get Stockage : {}", id);
        Optional<StockageDTO> stockageDTO = stockageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stockageDTO);
    }

    /**
     * {@code DELETE  /stockages/:id} : delete the "id" stockage.
     *
     * @param id the id of the stockageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStockage(@PathVariable Long id) {
        log.debug("REST request to delete Stockage : {}", id);
        stockageService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
