package com.m2i.sgpc.web.rest;

import com.m2i.sgpc.repository.FilialeRepository;
import com.m2i.sgpc.service.FilialeService;
import com.m2i.sgpc.service.dto.FilialeDTO;
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
 * REST controller for managing {@link com.m2i.sgpc.domain.Filiale}.
 */
@RestController
@RequestMapping("/api/filiales")
public class FilialeResource {

    private final Logger log = LoggerFactory.getLogger(FilialeResource.class);

    private static final String ENTITY_NAME = "filiale";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FilialeService filialeService;

    private final FilialeRepository filialeRepository;

    public FilialeResource(FilialeService filialeService, FilialeRepository filialeRepository) {
        this.filialeService = filialeService;
        this.filialeRepository = filialeRepository;
    }

    /**
     * {@code POST  /filiales} : Create a new filiale.
     *
     * @param filialeDTO the filialeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new filialeDTO, or with status {@code 400 (Bad Request)} if the filiale has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FilialeDTO> createFiliale(@RequestBody FilialeDTO filialeDTO) throws URISyntaxException {
        log.debug("REST request to save Filiale : {}", filialeDTO);
        if (filialeDTO.getId() != null) {
            throw new BadRequestAlertException("A new filiale cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FilialeDTO result = filialeService.save(filialeDTO);
        return ResponseEntity
            .created(new URI("/api/filiales/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /filiales/:id} : Updates an existing filiale.
     *
     * @param id the id of the filialeDTO to save.
     * @param filialeDTO the filialeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated filialeDTO,
     * or with status {@code 400 (Bad Request)} if the filialeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the filialeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FilialeDTO> updateFiliale(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FilialeDTO filialeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Filiale : {}, {}", id, filialeDTO);
        if (filialeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, filialeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!filialeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FilialeDTO result = filialeService.update(filialeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, filialeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /filiales/:id} : Partial updates given fields of an existing filiale, field will ignore if it is null
     *
     * @param id the id of the filialeDTO to save.
     * @param filialeDTO the filialeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated filialeDTO,
     * or with status {@code 400 (Bad Request)} if the filialeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the filialeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the filialeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FilialeDTO> partialUpdateFiliale(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FilialeDTO filialeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Filiale partially : {}, {}", id, filialeDTO);
        if (filialeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, filialeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!filialeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FilialeDTO> result = filialeService.partialUpdate(filialeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, filialeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /filiales} : get all the filiales.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of filiales in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FilialeDTO>> getAllFiliales(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Filiales");
        Page<FilialeDTO> page = filialeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /filiales/:id} : get the "id" filiale.
     *
     * @param id the id of the filialeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the filialeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FilialeDTO> getFiliale(@PathVariable Long id) {
        log.debug("REST request to get Filiale : {}", id);
        Optional<FilialeDTO> filialeDTO = filialeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(filialeDTO);
    }

    /**
     * {@code DELETE  /filiales/:id} : delete the "id" filiale.
     *
     * @param id the id of the filialeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFiliale(@PathVariable Long id) {
        log.debug("REST request to delete Filiale : {}", id);
        filialeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
