package com.m2i.sgpc.web.rest;

import static com.m2i.sgpc.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.m2i.sgpc.IntegrationTest;
import com.m2i.sgpc.domain.Colisage;
import com.m2i.sgpc.repository.ColisageRepository;
import com.m2i.sgpc.service.dto.ColisageDTO;
import com.m2i.sgpc.service.mapper.ColisageMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ColisageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ColisageResourceIT {

    private static final String DEFAULT_DESTINATION = "AAAAAAAAAA";
    private static final String UPDATED_DESTINATION = "BBBBBBBBBB";

    private static final String DEFAULT_CANAL = "AAAAAAAAAA";
    private static final String UPDATED_CANAL = "BBBBBBBBBB";

    private static final String DEFAULT_RECU_PAR = "AAAAAAAAAA";
    private static final String UPDATED_RECU_PAR = "BBBBBBBBBB";

    private static final Boolean DEFAULT_EST_RECU = false;
    private static final Boolean UPDATED_EST_RECU = true;

    private static final ZonedDateTime DEFAULT_DATE_CREATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_CREATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/colisages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ColisageRepository colisageRepository;

    @Autowired
    private ColisageMapper colisageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restColisageMockMvc;

    private Colisage colisage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Colisage createEntity(EntityManager em) {
        Colisage colisage = new Colisage()
            .destination(DEFAULT_DESTINATION)
            .canal(DEFAULT_CANAL)
            .recuPar(DEFAULT_RECU_PAR)
            .estRecu(DEFAULT_EST_RECU)
            .dateCreation(DEFAULT_DATE_CREATION);
        return colisage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Colisage createUpdatedEntity(EntityManager em) {
        Colisage colisage = new Colisage()
            .destination(UPDATED_DESTINATION)
            .canal(UPDATED_CANAL)
            .recuPar(UPDATED_RECU_PAR)
            .estRecu(UPDATED_EST_RECU)
            .dateCreation(UPDATED_DATE_CREATION);
        return colisage;
    }

    @BeforeEach
    public void initTest() {
        colisage = createEntity(em);
    }

    @Test
    @Transactional
    void createColisage() throws Exception {
        int databaseSizeBeforeCreate = colisageRepository.findAll().size();
        // Create the Colisage
        ColisageDTO colisageDTO = colisageMapper.toDto(colisage);
        restColisageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(colisageDTO)))
            .andExpect(status().isCreated());

        // Validate the Colisage in the database
        List<Colisage> colisageList = colisageRepository.findAll();
        assertThat(colisageList).hasSize(databaseSizeBeforeCreate + 1);
        Colisage testColisage = colisageList.get(colisageList.size() - 1);
        assertThat(testColisage.getDestination()).isEqualTo(DEFAULT_DESTINATION);
        assertThat(testColisage.getCanal()).isEqualTo(DEFAULT_CANAL);
        assertThat(testColisage.getRecuPar()).isEqualTo(DEFAULT_RECU_PAR);
        assertThat(testColisage.getEstRecu()).isEqualTo(DEFAULT_EST_RECU);
        assertThat(testColisage.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
    }

    @Test
    @Transactional
    void createColisageWithExistingId() throws Exception {
        // Create the Colisage with an existing ID
        colisage.setId(1L);
        ColisageDTO colisageDTO = colisageMapper.toDto(colisage);

        int databaseSizeBeforeCreate = colisageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restColisageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(colisageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Colisage in the database
        List<Colisage> colisageList = colisageRepository.findAll();
        assertThat(colisageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllColisages() throws Exception {
        // Initialize the database
        colisageRepository.saveAndFlush(colisage);

        // Get all the colisageList
        restColisageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(colisage.getId().intValue())))
            .andExpect(jsonPath("$.[*].destination").value(hasItem(DEFAULT_DESTINATION)))
            .andExpect(jsonPath("$.[*].canal").value(hasItem(DEFAULT_CANAL)))
            .andExpect(jsonPath("$.[*].recuPar").value(hasItem(DEFAULT_RECU_PAR)))
            .andExpect(jsonPath("$.[*].estRecu").value(hasItem(DEFAULT_EST_RECU.booleanValue())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(sameInstant(DEFAULT_DATE_CREATION))));
    }

    @Test
    @Transactional
    void getColisage() throws Exception {
        // Initialize the database
        colisageRepository.saveAndFlush(colisage);

        // Get the colisage
        restColisageMockMvc
            .perform(get(ENTITY_API_URL_ID, colisage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(colisage.getId().intValue()))
            .andExpect(jsonPath("$.destination").value(DEFAULT_DESTINATION))
            .andExpect(jsonPath("$.canal").value(DEFAULT_CANAL))
            .andExpect(jsonPath("$.recuPar").value(DEFAULT_RECU_PAR))
            .andExpect(jsonPath("$.estRecu").value(DEFAULT_EST_RECU.booleanValue()))
            .andExpect(jsonPath("$.dateCreation").value(sameInstant(DEFAULT_DATE_CREATION)));
    }

    @Test
    @Transactional
    void getNonExistingColisage() throws Exception {
        // Get the colisage
        restColisageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingColisage() throws Exception {
        // Initialize the database
        colisageRepository.saveAndFlush(colisage);

        int databaseSizeBeforeUpdate = colisageRepository.findAll().size();

        // Update the colisage
        Colisage updatedColisage = colisageRepository.findById(colisage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedColisage are not directly saved in db
        em.detach(updatedColisage);
        updatedColisage
            .destination(UPDATED_DESTINATION)
            .canal(UPDATED_CANAL)
            .recuPar(UPDATED_RECU_PAR)
            .estRecu(UPDATED_EST_RECU)
            .dateCreation(UPDATED_DATE_CREATION);
        ColisageDTO colisageDTO = colisageMapper.toDto(updatedColisage);

        restColisageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, colisageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(colisageDTO))
            )
            .andExpect(status().isOk());

        // Validate the Colisage in the database
        List<Colisage> colisageList = colisageRepository.findAll();
        assertThat(colisageList).hasSize(databaseSizeBeforeUpdate);
        Colisage testColisage = colisageList.get(colisageList.size() - 1);
        assertThat(testColisage.getDestination()).isEqualTo(UPDATED_DESTINATION);
        assertThat(testColisage.getCanal()).isEqualTo(UPDATED_CANAL);
        assertThat(testColisage.getRecuPar()).isEqualTo(UPDATED_RECU_PAR);
        assertThat(testColisage.getEstRecu()).isEqualTo(UPDATED_EST_RECU);
        assertThat(testColisage.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void putNonExistingColisage() throws Exception {
        int databaseSizeBeforeUpdate = colisageRepository.findAll().size();
        colisage.setId(longCount.incrementAndGet());

        // Create the Colisage
        ColisageDTO colisageDTO = colisageMapper.toDto(colisage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restColisageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, colisageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(colisageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Colisage in the database
        List<Colisage> colisageList = colisageRepository.findAll();
        assertThat(colisageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchColisage() throws Exception {
        int databaseSizeBeforeUpdate = colisageRepository.findAll().size();
        colisage.setId(longCount.incrementAndGet());

        // Create the Colisage
        ColisageDTO colisageDTO = colisageMapper.toDto(colisage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restColisageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(colisageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Colisage in the database
        List<Colisage> colisageList = colisageRepository.findAll();
        assertThat(colisageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamColisage() throws Exception {
        int databaseSizeBeforeUpdate = colisageRepository.findAll().size();
        colisage.setId(longCount.incrementAndGet());

        // Create the Colisage
        ColisageDTO colisageDTO = colisageMapper.toDto(colisage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restColisageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(colisageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Colisage in the database
        List<Colisage> colisageList = colisageRepository.findAll();
        assertThat(colisageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateColisageWithPatch() throws Exception {
        // Initialize the database
        colisageRepository.saveAndFlush(colisage);

        int databaseSizeBeforeUpdate = colisageRepository.findAll().size();

        // Update the colisage using partial update
        Colisage partialUpdatedColisage = new Colisage();
        partialUpdatedColisage.setId(colisage.getId());

        partialUpdatedColisage
            .destination(UPDATED_DESTINATION)
            .canal(UPDATED_CANAL)
            .recuPar(UPDATED_RECU_PAR)
            .dateCreation(UPDATED_DATE_CREATION);

        restColisageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedColisage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedColisage))
            )
            .andExpect(status().isOk());

        // Validate the Colisage in the database
        List<Colisage> colisageList = colisageRepository.findAll();
        assertThat(colisageList).hasSize(databaseSizeBeforeUpdate);
        Colisage testColisage = colisageList.get(colisageList.size() - 1);
        assertThat(testColisage.getDestination()).isEqualTo(UPDATED_DESTINATION);
        assertThat(testColisage.getCanal()).isEqualTo(UPDATED_CANAL);
        assertThat(testColisage.getRecuPar()).isEqualTo(UPDATED_RECU_PAR);
        assertThat(testColisage.getEstRecu()).isEqualTo(DEFAULT_EST_RECU);
        assertThat(testColisage.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void fullUpdateColisageWithPatch() throws Exception {
        // Initialize the database
        colisageRepository.saveAndFlush(colisage);

        int databaseSizeBeforeUpdate = colisageRepository.findAll().size();

        // Update the colisage using partial update
        Colisage partialUpdatedColisage = new Colisage();
        partialUpdatedColisage.setId(colisage.getId());

        partialUpdatedColisage
            .destination(UPDATED_DESTINATION)
            .canal(UPDATED_CANAL)
            .recuPar(UPDATED_RECU_PAR)
            .estRecu(UPDATED_EST_RECU)
            .dateCreation(UPDATED_DATE_CREATION);

        restColisageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedColisage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedColisage))
            )
            .andExpect(status().isOk());

        // Validate the Colisage in the database
        List<Colisage> colisageList = colisageRepository.findAll();
        assertThat(colisageList).hasSize(databaseSizeBeforeUpdate);
        Colisage testColisage = colisageList.get(colisageList.size() - 1);
        assertThat(testColisage.getDestination()).isEqualTo(UPDATED_DESTINATION);
        assertThat(testColisage.getCanal()).isEqualTo(UPDATED_CANAL);
        assertThat(testColisage.getRecuPar()).isEqualTo(UPDATED_RECU_PAR);
        assertThat(testColisage.getEstRecu()).isEqualTo(UPDATED_EST_RECU);
        assertThat(testColisage.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void patchNonExistingColisage() throws Exception {
        int databaseSizeBeforeUpdate = colisageRepository.findAll().size();
        colisage.setId(longCount.incrementAndGet());

        // Create the Colisage
        ColisageDTO colisageDTO = colisageMapper.toDto(colisage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restColisageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, colisageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(colisageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Colisage in the database
        List<Colisage> colisageList = colisageRepository.findAll();
        assertThat(colisageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchColisage() throws Exception {
        int databaseSizeBeforeUpdate = colisageRepository.findAll().size();
        colisage.setId(longCount.incrementAndGet());

        // Create the Colisage
        ColisageDTO colisageDTO = colisageMapper.toDto(colisage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restColisageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(colisageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Colisage in the database
        List<Colisage> colisageList = colisageRepository.findAll();
        assertThat(colisageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamColisage() throws Exception {
        int databaseSizeBeforeUpdate = colisageRepository.findAll().size();
        colisage.setId(longCount.incrementAndGet());

        // Create the Colisage
        ColisageDTO colisageDTO = colisageMapper.toDto(colisage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restColisageMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(colisageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Colisage in the database
        List<Colisage> colisageList = colisageRepository.findAll();
        assertThat(colisageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteColisage() throws Exception {
        // Initialize the database
        colisageRepository.saveAndFlush(colisage);

        int databaseSizeBeforeDelete = colisageRepository.findAll().size();

        // Delete the colisage
        restColisageMockMvc
            .perform(delete(ENTITY_API_URL_ID, colisage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Colisage> colisageList = colisageRepository.findAll();
        assertThat(colisageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
