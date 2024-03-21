package com.m2i.sgpc.web.rest;

import com.m2i.sgpc.repository.ProductionRepository;
import com.m2i.sgpc.service.ProductionService;
import com.m2i.sgpc.service.dto.ProductionDTO;
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
 * REST controller for managing {@link com.m2i.sgpc.domain.Production}.
 */
@RestController
@RequestMapping("/api/productions")
public class ProductionResource {

    private final Logger log = LoggerFactory.getLogger(ProductionResource.class);

    private static final String ENTITY_NAME = "production";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductionService productionService;

    private final ProductionRepository productionRepository;

    public ProductionResource(ProductionService productionService, ProductionRepository productionRepository) {
        this.productionService = productionService;
        this.productionRepository = productionRepository;
    }

    /**
     * {@code POST  /productions} : Create a new production.
     *
     * @param productionDTO the productionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productionDTO, or with status {@code 400 (Bad Request)} if the production has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductionDTO> createProduction(@RequestBody ProductionDTO productionDTO) throws URISyntaxException {
        log.debug("REST request to save Production : {}", productionDTO);
        if (productionDTO.getId() != null) {
            throw new BadRequestAlertException("A new production cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductionDTO result = productionService.save(productionDTO);
        return ResponseEntity
            .created(new URI("/api/productions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /productions/:id} : Updates an existing production.
     *
     * @param id the id of the productionDTO to save.
     * @param productionDTO the productionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productionDTO,
     * or with status {@code 400 (Bad Request)} if the productionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductionDTO> updateProduction(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductionDTO productionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Production : {}, {}", id, productionDTO);
        if (productionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductionDTO result = productionService.update(productionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /productions/:id} : Partial updates given fields of an existing production, field will ignore if it is null
     *
     * @param id the id of the productionDTO to save.
     * @param productionDTO the productionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productionDTO,
     * or with status {@code 400 (Bad Request)} if the productionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductionDTO> partialUpdateProduction(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductionDTO productionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Production partially : {}, {}", id, productionDTO);
        if (productionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductionDTO> result = productionService.partialUpdate(productionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /productions} : get all the productions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProductionDTO>> getAllProductions(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Productions");
        Page<ProductionDTO> page = productionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/courrier")
    public List<ProductionDTO> getAllProductionsCourrier(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Productions");
        return productionService.findAllCourrier();
    }

    /**
     * {@code GET  /productions/:id} : get the "id" production.
     *
     * @param id the id of the productionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductionDTO> getProduction(@PathVariable Long id) {
        log.debug("REST request to get Production : {}", id);
        Optional<ProductionDTO> productionDTO = productionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productionDTO);
    }

    /**
     * {@code DELETE  /productions/:id} : delete the "id" production.
     *
     * @param id the id of the productionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduction(@PathVariable Long id) {
        log.debug("REST request to delete Production : {}", id);
        productionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
