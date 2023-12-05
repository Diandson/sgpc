package com.m2i.sgpc.web.rest;

import static com.m2i.sgpc.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.m2i.sgpc.IntegrationTest;
import com.m2i.sgpc.domain.Production;
import com.m2i.sgpc.domain.enumeration.ETATPRODUCTION;
import com.m2i.sgpc.repository.ProductionRepository;
import com.m2i.sgpc.service.dto.ProductionDTO;
import com.m2i.sgpc.service.mapper.ProductionMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ProductionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductionResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FICHIER = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FICHIER = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FICHIER_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FICHIER_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_FINISH = false;
    private static final Boolean UPDATED_FINISH = true;

    private static final ETATPRODUCTION DEFAULT_ETAT = ETATPRODUCTION.ATTENTE;
    private static final ETATPRODUCTION UPDATED_ETAT = ETATPRODUCTION.EN_COURS;

    private static final String DEFAULT_VALIDER_PAR = "AAAAAAAAAA";
    private static final String UPDATED_VALIDER_PAR = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DEPOT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEPOT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final ZonedDateTime DEFAULT_DATE_VALIDER = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_VALIDER = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final LocalDate DEFAULT_DATE_OUVERT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OUVERT = LocalDate.now(ZoneId.systemDefault());

    private static final ZonedDateTime DEFAULT_DATE_CREATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_CREATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/productions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductionRepository productionRepository;

    @Autowired
    private ProductionMapper productionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductionMockMvc;

    private Production production;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Production createEntity(EntityManager em) {
        Production production = new Production()
            .libelle(DEFAULT_LIBELLE)
            .fichier(DEFAULT_FICHIER)
            .fichierContentType(DEFAULT_FICHIER_CONTENT_TYPE)
            .finish(DEFAULT_FINISH)
            .etat(DEFAULT_ETAT)
            .validerPar(DEFAULT_VALIDER_PAR)
            .dateDepot(DEFAULT_DATE_DEPOT)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN)
            .dateValider(DEFAULT_DATE_VALIDER)
            .dateOuvert(DEFAULT_DATE_OUVERT)
            .dateCreation(DEFAULT_DATE_CREATION);
        return production;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Production createUpdatedEntity(EntityManager em) {
        Production production = new Production()
            .libelle(UPDATED_LIBELLE)
            .fichier(UPDATED_FICHIER)
            .fichierContentType(UPDATED_FICHIER_CONTENT_TYPE)
            .finish(UPDATED_FINISH)
            .etat(UPDATED_ETAT)
            .validerPar(UPDATED_VALIDER_PAR)
            .dateDepot(UPDATED_DATE_DEPOT)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .dateValider(UPDATED_DATE_VALIDER)
            .dateOuvert(UPDATED_DATE_OUVERT)
            .dateCreation(UPDATED_DATE_CREATION);
        return production;
    }

    @BeforeEach
    public void initTest() {
        production = createEntity(em);
    }

    @Test
    @Transactional
    void createProduction() throws Exception {
        int databaseSizeBeforeCreate = productionRepository.findAll().size();
        // Create the Production
        ProductionDTO productionDTO = productionMapper.toDto(production);
        restProductionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productionDTO)))
            .andExpect(status().isCreated());

        // Validate the Production in the database
        List<Production> productionList = productionRepository.findAll();
        assertThat(productionList).hasSize(databaseSizeBeforeCreate + 1);
        Production testProduction = productionList.get(productionList.size() - 1);
        assertThat(testProduction.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testProduction.getFichier()).isEqualTo(DEFAULT_FICHIER);
        assertThat(testProduction.getFichierContentType()).isEqualTo(DEFAULT_FICHIER_CONTENT_TYPE);
        assertThat(testProduction.getFinish()).isEqualTo(DEFAULT_FINISH);
        assertThat(testProduction.getEtat()).isEqualTo(DEFAULT_ETAT);
        assertThat(testProduction.getValiderPar()).isEqualTo(DEFAULT_VALIDER_PAR);
        assertThat(testProduction.getDateDepot()).isEqualTo(DEFAULT_DATE_DEPOT);
        assertThat(testProduction.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testProduction.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testProduction.getDateValider()).isEqualTo(DEFAULT_DATE_VALIDER);
        assertThat(testProduction.getDateOuvert()).isEqualTo(DEFAULT_DATE_OUVERT);
        assertThat(testProduction.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
    }

    @Test
    @Transactional
    void createProductionWithExistingId() throws Exception {
        // Create the Production with an existing ID
        production.setId(1L);
        ProductionDTO productionDTO = productionMapper.toDto(production);

        int databaseSizeBeforeCreate = productionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Production in the database
        List<Production> productionList = productionRepository.findAll();
        assertThat(productionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductions() throws Exception {
        // Initialize the database
        productionRepository.saveAndFlush(production);

        // Get all the productionList
        restProductionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(production.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].fichierContentType").value(hasItem(DEFAULT_FICHIER_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fichier").value(hasItem(Base64Utils.encodeToString(DEFAULT_FICHIER))))
            .andExpect(jsonPath("$.[*].finish").value(hasItem(DEFAULT_FINISH.booleanValue())))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())))
            .andExpect(jsonPath("$.[*].validerPar").value(hasItem(DEFAULT_VALIDER_PAR)))
            .andExpect(jsonPath("$.[*].dateDepot").value(hasItem(DEFAULT_DATE_DEPOT.toString())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].dateValider").value(hasItem(sameInstant(DEFAULT_DATE_VALIDER))))
            .andExpect(jsonPath("$.[*].dateOuvert").value(hasItem(DEFAULT_DATE_OUVERT.toString())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(sameInstant(DEFAULT_DATE_CREATION))));
    }

    @Test
    @Transactional
    void getProduction() throws Exception {
        // Initialize the database
        productionRepository.saveAndFlush(production);

        // Get the production
        restProductionMockMvc
            .perform(get(ENTITY_API_URL_ID, production.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(production.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.fichierContentType").value(DEFAULT_FICHIER_CONTENT_TYPE))
            .andExpect(jsonPath("$.fichier").value(Base64Utils.encodeToString(DEFAULT_FICHIER)))
            .andExpect(jsonPath("$.finish").value(DEFAULT_FINISH.booleanValue()))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT.toString()))
            .andExpect(jsonPath("$.validerPar").value(DEFAULT_VALIDER_PAR))
            .andExpect(jsonPath("$.dateDepot").value(DEFAULT_DATE_DEPOT.toString()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.dateValider").value(sameInstant(DEFAULT_DATE_VALIDER)))
            .andExpect(jsonPath("$.dateOuvert").value(DEFAULT_DATE_OUVERT.toString()))
            .andExpect(jsonPath("$.dateCreation").value(sameInstant(DEFAULT_DATE_CREATION)));
    }

    @Test
    @Transactional
    void getNonExistingProduction() throws Exception {
        // Get the production
        restProductionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProduction() throws Exception {
        // Initialize the database
        productionRepository.saveAndFlush(production);

        int databaseSizeBeforeUpdate = productionRepository.findAll().size();

        // Update the production
        Production updatedProduction = productionRepository.findById(production.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProduction are not directly saved in db
        em.detach(updatedProduction);
        updatedProduction
            .libelle(UPDATED_LIBELLE)
            .fichier(UPDATED_FICHIER)
            .fichierContentType(UPDATED_FICHIER_CONTENT_TYPE)
            .finish(UPDATED_FINISH)
            .etat(UPDATED_ETAT)
            .validerPar(UPDATED_VALIDER_PAR)
            .dateDepot(UPDATED_DATE_DEPOT)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .dateValider(UPDATED_DATE_VALIDER)
            .dateOuvert(UPDATED_DATE_OUVERT)
            .dateCreation(UPDATED_DATE_CREATION);
        ProductionDTO productionDTO = productionMapper.toDto(updatedProduction);

        restProductionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Production in the database
        List<Production> productionList = productionRepository.findAll();
        assertThat(productionList).hasSize(databaseSizeBeforeUpdate);
        Production testProduction = productionList.get(productionList.size() - 1);
        assertThat(testProduction.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testProduction.getFichier()).isEqualTo(UPDATED_FICHIER);
        assertThat(testProduction.getFichierContentType()).isEqualTo(UPDATED_FICHIER_CONTENT_TYPE);
        assertThat(testProduction.getFinish()).isEqualTo(UPDATED_FINISH);
        assertThat(testProduction.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testProduction.getValiderPar()).isEqualTo(UPDATED_VALIDER_PAR);
        assertThat(testProduction.getDateDepot()).isEqualTo(UPDATED_DATE_DEPOT);
        assertThat(testProduction.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testProduction.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testProduction.getDateValider()).isEqualTo(UPDATED_DATE_VALIDER);
        assertThat(testProduction.getDateOuvert()).isEqualTo(UPDATED_DATE_OUVERT);
        assertThat(testProduction.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void putNonExistingProduction() throws Exception {
        int databaseSizeBeforeUpdate = productionRepository.findAll().size();
        production.setId(longCount.incrementAndGet());

        // Create the Production
        ProductionDTO productionDTO = productionMapper.toDto(production);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Production in the database
        List<Production> productionList = productionRepository.findAll();
        assertThat(productionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProduction() throws Exception {
        int databaseSizeBeforeUpdate = productionRepository.findAll().size();
        production.setId(longCount.incrementAndGet());

        // Create the Production
        ProductionDTO productionDTO = productionMapper.toDto(production);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Production in the database
        List<Production> productionList = productionRepository.findAll();
        assertThat(productionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProduction() throws Exception {
        int databaseSizeBeforeUpdate = productionRepository.findAll().size();
        production.setId(longCount.incrementAndGet());

        // Create the Production
        ProductionDTO productionDTO = productionMapper.toDto(production);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Production in the database
        List<Production> productionList = productionRepository.findAll();
        assertThat(productionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductionWithPatch() throws Exception {
        // Initialize the database
        productionRepository.saveAndFlush(production);

        int databaseSizeBeforeUpdate = productionRepository.findAll().size();

        // Update the production using partial update
        Production partialUpdatedProduction = new Production();
        partialUpdatedProduction.setId(production.getId());

        partialUpdatedProduction
            .libelle(UPDATED_LIBELLE)
            .etat(UPDATED_ETAT)
            .validerPar(UPDATED_VALIDER_PAR)
            .dateFin(UPDATED_DATE_FIN)
            .dateOuvert(UPDATED_DATE_OUVERT);

        restProductionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProduction))
            )
            .andExpect(status().isOk());

        // Validate the Production in the database
        List<Production> productionList = productionRepository.findAll();
        assertThat(productionList).hasSize(databaseSizeBeforeUpdate);
        Production testProduction = productionList.get(productionList.size() - 1);
        assertThat(testProduction.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testProduction.getFichier()).isEqualTo(DEFAULT_FICHIER);
        assertThat(testProduction.getFichierContentType()).isEqualTo(DEFAULT_FICHIER_CONTENT_TYPE);
        assertThat(testProduction.getFinish()).isEqualTo(DEFAULT_FINISH);
        assertThat(testProduction.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testProduction.getValiderPar()).isEqualTo(UPDATED_VALIDER_PAR);
        assertThat(testProduction.getDateDepot()).isEqualTo(DEFAULT_DATE_DEPOT);
        assertThat(testProduction.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testProduction.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testProduction.getDateValider()).isEqualTo(DEFAULT_DATE_VALIDER);
        assertThat(testProduction.getDateOuvert()).isEqualTo(UPDATED_DATE_OUVERT);
        assertThat(testProduction.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
    }

    @Test
    @Transactional
    void fullUpdateProductionWithPatch() throws Exception {
        // Initialize the database
        productionRepository.saveAndFlush(production);

        int databaseSizeBeforeUpdate = productionRepository.findAll().size();

        // Update the production using partial update
        Production partialUpdatedProduction = new Production();
        partialUpdatedProduction.setId(production.getId());

        partialUpdatedProduction
            .libelle(UPDATED_LIBELLE)
            .fichier(UPDATED_FICHIER)
            .fichierContentType(UPDATED_FICHIER_CONTENT_TYPE)
            .finish(UPDATED_FINISH)
            .etat(UPDATED_ETAT)
            .validerPar(UPDATED_VALIDER_PAR)
            .dateDepot(UPDATED_DATE_DEPOT)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .dateValider(UPDATED_DATE_VALIDER)
            .dateOuvert(UPDATED_DATE_OUVERT)
            .dateCreation(UPDATED_DATE_CREATION);

        restProductionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProduction))
            )
            .andExpect(status().isOk());

        // Validate the Production in the database
        List<Production> productionList = productionRepository.findAll();
        assertThat(productionList).hasSize(databaseSizeBeforeUpdate);
        Production testProduction = productionList.get(productionList.size() - 1);
        assertThat(testProduction.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testProduction.getFichier()).isEqualTo(UPDATED_FICHIER);
        assertThat(testProduction.getFichierContentType()).isEqualTo(UPDATED_FICHIER_CONTENT_TYPE);
        assertThat(testProduction.getFinish()).isEqualTo(UPDATED_FINISH);
        assertThat(testProduction.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testProduction.getValiderPar()).isEqualTo(UPDATED_VALIDER_PAR);
        assertThat(testProduction.getDateDepot()).isEqualTo(UPDATED_DATE_DEPOT);
        assertThat(testProduction.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testProduction.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testProduction.getDateValider()).isEqualTo(UPDATED_DATE_VALIDER);
        assertThat(testProduction.getDateOuvert()).isEqualTo(UPDATED_DATE_OUVERT);
        assertThat(testProduction.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void patchNonExistingProduction() throws Exception {
        int databaseSizeBeforeUpdate = productionRepository.findAll().size();
        production.setId(longCount.incrementAndGet());

        // Create the Production
        ProductionDTO productionDTO = productionMapper.toDto(production);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Production in the database
        List<Production> productionList = productionRepository.findAll();
        assertThat(productionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProduction() throws Exception {
        int databaseSizeBeforeUpdate = productionRepository.findAll().size();
        production.setId(longCount.incrementAndGet());

        // Create the Production
        ProductionDTO productionDTO = productionMapper.toDto(production);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Production in the database
        List<Production> productionList = productionRepository.findAll();
        assertThat(productionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProduction() throws Exception {
        int databaseSizeBeforeUpdate = productionRepository.findAll().size();
        production.setId(longCount.incrementAndGet());

        // Create the Production
        ProductionDTO productionDTO = productionMapper.toDto(production);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(productionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Production in the database
        List<Production> productionList = productionRepository.findAll();
        assertThat(productionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProduction() throws Exception {
        // Initialize the database
        productionRepository.saveAndFlush(production);

        int databaseSizeBeforeDelete = productionRepository.findAll().size();

        // Delete the production
        restProductionMockMvc
            .perform(delete(ENTITY_API_URL_ID, production.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Production> productionList = productionRepository.findAll();
        assertThat(productionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
