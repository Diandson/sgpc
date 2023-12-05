package com.m2i.sgpc.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.m2i.sgpc.IntegrationTest;
import com.m2i.sgpc.domain.Filiale;
import com.m2i.sgpc.repository.FilialeRepository;
import com.m2i.sgpc.service.dto.FilialeDTO;
import com.m2i.sgpc.service.mapper.FilialeMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link FilialeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FilialeResourceIT {

    private static final String DEFAULT_DENOMINATION = "AAAAAAAAAA";
    private static final String UPDATED_DENOMINATION = "BBBBBBBBBB";

    private static final String DEFAULT_SIGLE = "AAAAAAAAAA";
    private static final String UPDATED_SIGLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/filiales";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FilialeRepository filialeRepository;

    @Autowired
    private FilialeMapper filialeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFilialeMockMvc;

    private Filiale filiale;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Filiale createEntity(EntityManager em) {
        Filiale filiale = new Filiale().denomination(DEFAULT_DENOMINATION).sigle(DEFAULT_SIGLE);
        return filiale;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Filiale createUpdatedEntity(EntityManager em) {
        Filiale filiale = new Filiale().denomination(UPDATED_DENOMINATION).sigle(UPDATED_SIGLE);
        return filiale;
    }

    @BeforeEach
    public void initTest() {
        filiale = createEntity(em);
    }

    @Test
    @Transactional
    void createFiliale() throws Exception {
        int databaseSizeBeforeCreate = filialeRepository.findAll().size();
        // Create the Filiale
        FilialeDTO filialeDTO = filialeMapper.toDto(filiale);
        restFilialeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filialeDTO)))
            .andExpect(status().isCreated());

        // Validate the Filiale in the database
        List<Filiale> filialeList = filialeRepository.findAll();
        assertThat(filialeList).hasSize(databaseSizeBeforeCreate + 1);
        Filiale testFiliale = filialeList.get(filialeList.size() - 1);
        assertThat(testFiliale.getDenomination()).isEqualTo(DEFAULT_DENOMINATION);
        assertThat(testFiliale.getSigle()).isEqualTo(DEFAULT_SIGLE);
    }

    @Test
    @Transactional
    void createFilialeWithExistingId() throws Exception {
        // Create the Filiale with an existing ID
        filiale.setId(1L);
        FilialeDTO filialeDTO = filialeMapper.toDto(filiale);

        int databaseSizeBeforeCreate = filialeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFilialeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filialeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Filiale in the database
        List<Filiale> filialeList = filialeRepository.findAll();
        assertThat(filialeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFiliales() throws Exception {
        // Initialize the database
        filialeRepository.saveAndFlush(filiale);

        // Get all the filialeList
        restFilialeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(filiale.getId().intValue())))
            .andExpect(jsonPath("$.[*].denomination").value(hasItem(DEFAULT_DENOMINATION)))
            .andExpect(jsonPath("$.[*].sigle").value(hasItem(DEFAULT_SIGLE)));
    }

    @Test
    @Transactional
    void getFiliale() throws Exception {
        // Initialize the database
        filialeRepository.saveAndFlush(filiale);

        // Get the filiale
        restFilialeMockMvc
            .perform(get(ENTITY_API_URL_ID, filiale.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(filiale.getId().intValue()))
            .andExpect(jsonPath("$.denomination").value(DEFAULT_DENOMINATION))
            .andExpect(jsonPath("$.sigle").value(DEFAULT_SIGLE));
    }

    @Test
    @Transactional
    void getNonExistingFiliale() throws Exception {
        // Get the filiale
        restFilialeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFiliale() throws Exception {
        // Initialize the database
        filialeRepository.saveAndFlush(filiale);

        int databaseSizeBeforeUpdate = filialeRepository.findAll().size();

        // Update the filiale
        Filiale updatedFiliale = filialeRepository.findById(filiale.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFiliale are not directly saved in db
        em.detach(updatedFiliale);
        updatedFiliale.denomination(UPDATED_DENOMINATION).sigle(UPDATED_SIGLE);
        FilialeDTO filialeDTO = filialeMapper.toDto(updatedFiliale);

        restFilialeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, filialeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filialeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Filiale in the database
        List<Filiale> filialeList = filialeRepository.findAll();
        assertThat(filialeList).hasSize(databaseSizeBeforeUpdate);
        Filiale testFiliale = filialeList.get(filialeList.size() - 1);
        assertThat(testFiliale.getDenomination()).isEqualTo(UPDATED_DENOMINATION);
        assertThat(testFiliale.getSigle()).isEqualTo(UPDATED_SIGLE);
    }

    @Test
    @Transactional
    void putNonExistingFiliale() throws Exception {
        int databaseSizeBeforeUpdate = filialeRepository.findAll().size();
        filiale.setId(longCount.incrementAndGet());

        // Create the Filiale
        FilialeDTO filialeDTO = filialeMapper.toDto(filiale);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFilialeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, filialeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filialeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Filiale in the database
        List<Filiale> filialeList = filialeRepository.findAll();
        assertThat(filialeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFiliale() throws Exception {
        int databaseSizeBeforeUpdate = filialeRepository.findAll().size();
        filiale.setId(longCount.incrementAndGet());

        // Create the Filiale
        FilialeDTO filialeDTO = filialeMapper.toDto(filiale);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilialeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filialeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Filiale in the database
        List<Filiale> filialeList = filialeRepository.findAll();
        assertThat(filialeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFiliale() throws Exception {
        int databaseSizeBeforeUpdate = filialeRepository.findAll().size();
        filiale.setId(longCount.incrementAndGet());

        // Create the Filiale
        FilialeDTO filialeDTO = filialeMapper.toDto(filiale);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilialeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filialeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Filiale in the database
        List<Filiale> filialeList = filialeRepository.findAll();
        assertThat(filialeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFilialeWithPatch() throws Exception {
        // Initialize the database
        filialeRepository.saveAndFlush(filiale);

        int databaseSizeBeforeUpdate = filialeRepository.findAll().size();

        // Update the filiale using partial update
        Filiale partialUpdatedFiliale = new Filiale();
        partialUpdatedFiliale.setId(filiale.getId());

        partialUpdatedFiliale.sigle(UPDATED_SIGLE);

        restFilialeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFiliale.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFiliale))
            )
            .andExpect(status().isOk());

        // Validate the Filiale in the database
        List<Filiale> filialeList = filialeRepository.findAll();
        assertThat(filialeList).hasSize(databaseSizeBeforeUpdate);
        Filiale testFiliale = filialeList.get(filialeList.size() - 1);
        assertThat(testFiliale.getDenomination()).isEqualTo(DEFAULT_DENOMINATION);
        assertThat(testFiliale.getSigle()).isEqualTo(UPDATED_SIGLE);
    }

    @Test
    @Transactional
    void fullUpdateFilialeWithPatch() throws Exception {
        // Initialize the database
        filialeRepository.saveAndFlush(filiale);

        int databaseSizeBeforeUpdate = filialeRepository.findAll().size();

        // Update the filiale using partial update
        Filiale partialUpdatedFiliale = new Filiale();
        partialUpdatedFiliale.setId(filiale.getId());

        partialUpdatedFiliale.denomination(UPDATED_DENOMINATION).sigle(UPDATED_SIGLE);

        restFilialeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFiliale.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFiliale))
            )
            .andExpect(status().isOk());

        // Validate the Filiale in the database
        List<Filiale> filialeList = filialeRepository.findAll();
        assertThat(filialeList).hasSize(databaseSizeBeforeUpdate);
        Filiale testFiliale = filialeList.get(filialeList.size() - 1);
        assertThat(testFiliale.getDenomination()).isEqualTo(UPDATED_DENOMINATION);
        assertThat(testFiliale.getSigle()).isEqualTo(UPDATED_SIGLE);
    }

    @Test
    @Transactional
    void patchNonExistingFiliale() throws Exception {
        int databaseSizeBeforeUpdate = filialeRepository.findAll().size();
        filiale.setId(longCount.incrementAndGet());

        // Create the Filiale
        FilialeDTO filialeDTO = filialeMapper.toDto(filiale);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFilialeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, filialeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(filialeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Filiale in the database
        List<Filiale> filialeList = filialeRepository.findAll();
        assertThat(filialeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFiliale() throws Exception {
        int databaseSizeBeforeUpdate = filialeRepository.findAll().size();
        filiale.setId(longCount.incrementAndGet());

        // Create the Filiale
        FilialeDTO filialeDTO = filialeMapper.toDto(filiale);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilialeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(filialeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Filiale in the database
        List<Filiale> filialeList = filialeRepository.findAll();
        assertThat(filialeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFiliale() throws Exception {
        int databaseSizeBeforeUpdate = filialeRepository.findAll().size();
        filiale.setId(longCount.incrementAndGet());

        // Create the Filiale
        FilialeDTO filialeDTO = filialeMapper.toDto(filiale);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilialeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(filialeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Filiale in the database
        List<Filiale> filialeList = filialeRepository.findAll();
        assertThat(filialeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFiliale() throws Exception {
        // Initialize the database
        filialeRepository.saveAndFlush(filiale);

        int databaseSizeBeforeDelete = filialeRepository.findAll().size();

        // Delete the filiale
        restFilialeMockMvc
            .perform(delete(ENTITY_API_URL_ID, filiale.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Filiale> filialeList = filialeRepository.findAll();
        assertThat(filialeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
