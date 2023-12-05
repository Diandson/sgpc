package com.m2i.sgpc.web.rest;

import com.m2i.sgpc.repository.ColisageRepository;
import com.m2i.sgpc.service.ColisageService;
import com.m2i.sgpc.service.dto.ColisageDTO;
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
 * REST controller for managing {@link com.m2i.sgpc.domain.Colisage}.
 */
@RestController
@RequestMapping("/api/colisages")
public class ColisageResource {

    private final Logger log = LoggerFactory.getLogger(ColisageResource.class);

    private static final String ENTITY_NAME = "colisage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ColisageService colisageService;

    private final ColisageRepository colisageRepository;

    public ColisageResource(ColisageService colisageService, ColisageRepository colisageRepository) {
        this.colisageService = colisageService;
        this.colisageRepository = colisageRepository;
    }

    /**
     * {@code POST  /colisages} : Create a new colisage.
     *
     * @param colisageDTO the colisageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new colisageDTO, or with status {@code 400 (Bad Request)} if the colisage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ColisageDTO> createColisage(@RequestBody ColisageDTO colisageDTO) throws URISyntaxException {
        log.debug("REST request to save Colisage : {}", colisageDTO);
        if (colisageDTO.getId() != null) {
            throw new BadRequestAlertException("A new colisage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ColisageDTO result = colisageService.save(colisageDTO);
        return ResponseEntity
            .created(new URI("/api/colisages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /colisages/:id} : Updates an existing colisage.
     *
     * @param id the id of the colisageDTO to save.
     * @param colisageDTO the colisageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated colisageDTO,
     * or with status {@code 400 (Bad Request)} if the colisageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the colisageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ColisageDTO> updateColisage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ColisageDTO colisageDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Colisage : {}, {}", id, colisageDTO);
        if (colisageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, colisageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!colisageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ColisageDTO result = colisageService.update(colisageDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, colisageDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /colisages/:id} : Partial updates given fields of an existing colisage, field will ignore if it is null
     *
     * @param id the id of the colisageDTO to save.
     * @param colisageDTO the colisageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated colisageDTO,
     * or with status {@code 400 (Bad Request)} if the colisageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the colisageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the colisageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ColisageDTO> partialUpdateColisage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ColisageDTO colisageDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Colisage partially : {}, {}", id, colisageDTO);
        if (colisageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, colisageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!colisageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ColisageDTO> result = colisageService.partialUpdate(colisageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, colisageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /colisages} : get all the colisages.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of colisages in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ColisageDTO>> getAllColisages(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Colisages");
        Page<ColisageDTO> page = colisageService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /colisages/:id} : get the "id" colisage.
     *
     * @param id the id of the colisageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the colisageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ColisageDTO> getColisage(@PathVariable Long id) {
        log.debug("REST request to get Colisage : {}", id);
        Optional<ColisageDTO> colisageDTO = colisageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(colisageDTO);
    }

    /**
     * {@code DELETE  /colisages/:id} : delete the "id" colisage.
     *
     * @param id the id of the colisageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteColisage(@PathVariable Long id) {
        log.debug("REST request to delete Colisage : {}", id);
        colisageService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
