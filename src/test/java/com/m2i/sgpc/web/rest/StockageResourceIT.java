package com.m2i.sgpc.web.rest;

import static com.m2i.sgpc.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.m2i.sgpc.IntegrationTest;
import com.m2i.sgpc.domain.Stockage;
import com.m2i.sgpc.repository.StockageRepository;
import com.m2i.sgpc.service.dto.StockageDTO;
import com.m2i.sgpc.service.mapper.StockageMapper;
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
 * Integration tests for the {@link StockageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StockageResourceIT {

    private static final String DEFAULT_DENOMINATION = "AAAAAAAAAA";
    private static final String UPDATED_DENOMINATION = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_QUANTITE = "AAAAAAAAAA";
    private static final String UPDATED_QUANTITE = "BBBBBBBBBB";

    private static final String DEFAULT_MODIFIER_PAR = "AAAAAAAAAA";
    private static final String UPDATED_MODIFIER_PAR = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_CREATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_CREATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/stockages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StockageRepository stockageRepository;

    @Autowired
    private StockageMapper stockageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStockageMockMvc;

    private Stockage stockage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Stockage createEntity(EntityManager em) {
        Stockage stockage = new Stockage()
            .denomination(DEFAULT_DENOMINATION)
            .code(DEFAULT_CODE)
            .quantite(DEFAULT_QUANTITE)
            .modifierPar(DEFAULT_MODIFIER_PAR)
            .dateCreation(DEFAULT_DATE_CREATION);
        return stockage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Stockage createUpdatedEntity(EntityManager em) {
        Stockage stockage = new Stockage()
            .denomination(UPDATED_DENOMINATION)
            .code(UPDATED_CODE)
            .quantite(UPDATED_QUANTITE)
            .modifierPar(UPDATED_MODIFIER_PAR)
            .dateCreation(UPDATED_DATE_CREATION);
        return stockage;
    }

    @BeforeEach
    public void initTest() {
        stockage = createEntity(em);
    }

    @Test
    @Transactional
    void createStockage() throws Exception {
        int databaseSizeBeforeCreate = stockageRepository.findAll().size();
        // Create the Stockage
        StockageDTO stockageDTO = stockageMapper.toDto(stockage);
        restStockageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stockageDTO)))
            .andExpect(status().isCreated());

        // Validate the Stockage in the database
        List<Stockage> stockageList = stockageRepository.findAll();
        assertThat(stockageList).hasSize(databaseSizeBeforeCreate + 1);
        Stockage testStockage = stockageList.get(stockageList.size() - 1);
        assertThat(testStockage.getDenomination()).isEqualTo(DEFAULT_DENOMINATION);
        assertThat(testStockage.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testStockage.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testStockage.getModifierPar()).isEqualTo(DEFAULT_MODIFIER_PAR);
        assertThat(testStockage.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
    }

    @Test
    @Transactional
    void createStockageWithExistingId() throws Exception {
        // Create the Stockage with an existing ID
        stockage.setId(1L);
        StockageDTO stockageDTO = stockageMapper.toDto(stockage);

        int databaseSizeBeforeCreate = stockageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stockageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Stockage in the database
        List<Stockage> stockageList = stockageRepository.findAll();
        assertThat(stockageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStockages() throws Exception {
        // Initialize the database
        stockageRepository.saveAndFlush(stockage);

        // Get all the stockageList
        restStockageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockage.getId().intValue())))
            .andExpect(jsonPath("$.[*].denomination").value(hasItem(DEFAULT_DENOMINATION)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.[*].modifierPar").value(hasItem(DEFAULT_MODIFIER_PAR)))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(sameInstant(DEFAULT_DATE_CREATION))));
    }

    @Test
    @Transactional
    void getStockage() throws Exception {
        // Initialize the database
        stockageRepository.saveAndFlush(stockage);

        // Get the stockage
        restStockageMockMvc
            .perform(get(ENTITY_API_URL_ID, stockage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stockage.getId().intValue()))
            .andExpect(jsonPath("$.denomination").value(DEFAULT_DENOMINATION))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE))
            .andExpect(jsonPath("$.modifierPar").value(DEFAULT_MODIFIER_PAR))
            .andExpect(jsonPath("$.dateCreation").value(sameInstant(DEFAULT_DATE_CREATION)));
    }

    @Test
    @Transactional
    void getNonExistingStockage() throws Exception {
        // Get the stockage
        restStockageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStockage() throws Exception {
        // Initialize the database
        stockageRepository.saveAndFlush(stockage);

        int databaseSizeBeforeUpdate = stockageRepository.findAll().size();

        // Update the stockage
        Stockage updatedStockage = stockageRepository.findById(stockage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStockage are not directly saved in db
        em.detach(updatedStockage);
        updatedStockage
            .denomination(UPDATED_DENOMINATION)
            .code(UPDATED_CODE)
            .quantite(UPDATED_QUANTITE)
            .modifierPar(UPDATED_MODIFIER_PAR)
            .dateCreation(UPDATED_DATE_CREATION);
        StockageDTO stockageDTO = stockageMapper.toDto(updatedStockage);

        restStockageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stockageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stockageDTO))
            )
            .andExpect(status().isOk());

        // Validate the Stockage in the database
        List<Stockage> stockageList = stockageRepository.findAll();
        assertThat(stockageList).hasSize(databaseSizeBeforeUpdate);
        Stockage testStockage = stockageList.get(stockageList.size() - 1);
        assertThat(testStockage.getDenomination()).isEqualTo(UPDATED_DENOMINATION);
        assertThat(testStockage.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testStockage.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testStockage.getModifierPar()).isEqualTo(UPDATED_MODIFIER_PAR);
        assertThat(testStockage.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void putNonExistingStockage() throws Exception {
        int databaseSizeBeforeUpdate = stockageRepository.findAll().size();
        stockage.setId(longCount.incrementAndGet());

        // Create the Stockage
        StockageDTO stockageDTO = stockageMapper.toDto(stockage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stockageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stockageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stockage in the database
        List<Stockage> stockageList = stockageRepository.findAll();
        assertThat(stockageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStockage() throws Exception {
        int databaseSizeBeforeUpdate = stockageRepository.findAll().size();
        stockage.setId(longCount.incrementAndGet());

        // Create the Stockage
        StockageDTO stockageDTO = stockageMapper.toDto(stockage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stockageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stockage in the database
        List<Stockage> stockageList = stockageRepository.findAll();
        assertThat(stockageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStockage() throws Exception {
        int databaseSizeBeforeUpdate = stockageRepository.findAll().size();
        stockage.setId(longCount.incrementAndGet());

        // Create the Stockage
        StockageDTO stockageDTO = stockageMapper.toDto(stockage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stockageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Stockage in the database
        List<Stockage> stockageList = stockageRepository.findAll();
        assertThat(stockageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStockageWithPatch() throws Exception {
        // Initialize the database
        stockageRepository.saveAndFlush(stockage);

        int databaseSizeBeforeUpdate = stockageRepository.findAll().size();

        // Update the stockage using partial update
        Stockage partialUpdatedStockage = new Stockage();
        partialUpdatedStockage.setId(stockage.getId());

        partialUpdatedStockage.code(UPDATED_CODE);

        restStockageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStockage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStockage))
            )
            .andExpect(status().isOk());

        // Validate the Stockage in the database
        List<Stockage> stockageList = stockageRepository.findAll();
        assertThat(stockageList).hasSize(databaseSizeBeforeUpdate);
        Stockage testStockage = stockageList.get(stockageList.size() - 1);
        assertThat(testStockage.getDenomination()).isEqualTo(DEFAULT_DENOMINATION);
        assertThat(testStockage.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testStockage.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testStockage.getModifierPar()).isEqualTo(DEFAULT_MODIFIER_PAR);
        assertThat(testStockage.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
    }

    @Test
    @Transactional
    void fullUpdateStockageWithPatch() throws Exception {
        // Initialize the database
        stockageRepository.saveAndFlush(stockage);

        int databaseSizeBeforeUpdate = stockageRepository.findAll().size();

        // Update the stockage using partial update
        Stockage partialUpdatedStockage = new Stockage();
        partialUpdatedStockage.setId(stockage.getId());

        partialUpdatedStockage
            .denomination(UPDATED_DENOMINATION)
            .code(UPDATED_CODE)
            .quantite(UPDATED_QUANTITE)
            .modifierPar(UPDATED_MODIFIER_PAR)
            .dateCreation(UPDATED_DATE_CREATION);

        restStockageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStockage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStockage))
            )
            .andExpect(status().isOk());

        // Validate the Stockage in the database
        List<Stockage> stockageList = stockageRepository.findAll();
        assertThat(stockageList).hasSize(databaseSizeBeforeUpdate);
        Stockage testStockage = stockageList.get(stockageList.size() - 1);
        assertThat(testStockage.getDenomination()).isEqualTo(UPDATED_DENOMINATION);
        assertThat(testStockage.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testStockage.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testStockage.getModifierPar()).isEqualTo(UPDATED_MODIFIER_PAR);
        assertThat(testStockage.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void patchNonExistingStockage() throws Exception {
        int databaseSizeBeforeUpdate = stockageRepository.findAll().size();
        stockage.setId(longCount.incrementAndGet());

        // Create the Stockage
        StockageDTO stockageDTO = stockageMapper.toDto(stockage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, stockageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stockageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stockage in the database
        List<Stockage> stockageList = stockageRepository.findAll();
        assertThat(stockageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStockage() throws Exception {
        int databaseSizeBeforeUpdate = stockageRepository.findAll().size();
        stockage.setId(longCount.incrementAndGet());

        // Create the Stockage
        StockageDTO stockageDTO = stockageMapper.toDto(stockage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stockageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stockage in the database
        List<Stockage> stockageList = stockageRepository.findAll();
        assertThat(stockageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStockage() throws Exception {
        int databaseSizeBeforeUpdate = stockageRepository.findAll().size();
        stockage.setId(longCount.incrementAndGet());

        // Create the Stockage
        StockageDTO stockageDTO = stockageMapper.toDto(stockage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockageMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(stockageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Stockage in the database
        List<Stockage> stockageList = stockageRepository.findAll();
        assertThat(stockageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStockage() throws Exception {
        // Initialize the database
        stockageRepository.saveAndFlush(stockage);

        int databaseSizeBeforeDelete = stockageRepository.findAll().size();

        // Delete the stockage
        restStockageMockMvc
            .perform(delete(ENTITY_API_URL_ID, stockage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Stockage> stockageList = stockageRepository.findAll();
        assertThat(stockageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
