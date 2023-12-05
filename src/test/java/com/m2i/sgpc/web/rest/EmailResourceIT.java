package com.m2i.sgpc.web.rest;

import static com.m2i.sgpc.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.m2i.sgpc.IntegrationTest;
import com.m2i.sgpc.domain.Email;
import com.m2i.sgpc.repository.EmailRepository;
import com.m2i.sgpc.service.dto.EmailDTO;
import com.m2i.sgpc.service.mapper.EmailMapper;
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
 * Integration tests for the {@link EmailResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmailResourceIT {

    private static final String DEFAULT_OBJET = "AAAAAAAAAA";
    private static final String UPDATED_OBJET = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENU = "AAAAAAAAAA";
    private static final String UPDATED_CONTENU = "BBBBBBBBBB";

    private static final String DEFAULT_DESTINATAIRE = "AAAAAAAAAA";
    private static final String UPDATED_DESTINATAIRE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_ENVOI = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_ENVOI = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/emails";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private EmailMapper emailMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmailMockMvc;

    private Email email;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Email createEntity(EntityManager em) {
        Email email = new Email()
            .objet(DEFAULT_OBJET)
            .contenu(DEFAULT_CONTENU)
            .destinataire(DEFAULT_DESTINATAIRE)
            .dateEnvoi(DEFAULT_DATE_ENVOI);
        return email;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Email createUpdatedEntity(EntityManager em) {
        Email email = new Email()
            .objet(UPDATED_OBJET)
            .contenu(UPDATED_CONTENU)
            .destinataire(UPDATED_DESTINATAIRE)
            .dateEnvoi(UPDATED_DATE_ENVOI);
        return email;
    }

    @BeforeEach
    public void initTest() {
        email = createEntity(em);
    }

    @Test
    @Transactional
    void createEmail() throws Exception {
        int databaseSizeBeforeCreate = emailRepository.findAll().size();
        // Create the Email
        EmailDTO emailDTO = emailMapper.toDto(email);
        restEmailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(emailDTO)))
            .andExpect(status().isCreated());

        // Validate the Email in the database
        List<Email> emailList = emailRepository.findAll();
        assertThat(emailList).hasSize(databaseSizeBeforeCreate + 1);
        Email testEmail = emailList.get(emailList.size() - 1);
        assertThat(testEmail.getObjet()).isEqualTo(DEFAULT_OBJET);
        assertThat(testEmail.getContenu()).isEqualTo(DEFAULT_CONTENU);
        assertThat(testEmail.getDestinataire()).isEqualTo(DEFAULT_DESTINATAIRE);
        assertThat(testEmail.getDateEnvoi()).isEqualTo(DEFAULT_DATE_ENVOI);
    }

    @Test
    @Transactional
    void createEmailWithExistingId() throws Exception {
        // Create the Email with an existing ID
        email.setId(1L);
        EmailDTO emailDTO = emailMapper.toDto(email);

        int databaseSizeBeforeCreate = emailRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(emailDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Email in the database
        List<Email> emailList = emailRepository.findAll();
        assertThat(emailList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEmails() throws Exception {
        // Initialize the database
        emailRepository.saveAndFlush(email);

        // Get all the emailList
        restEmailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(email.getId().intValue())))
            .andExpect(jsonPath("$.[*].objet").value(hasItem(DEFAULT_OBJET)))
            .andExpect(jsonPath("$.[*].contenu").value(hasItem(DEFAULT_CONTENU)))
            .andExpect(jsonPath("$.[*].destinataire").value(hasItem(DEFAULT_DESTINATAIRE)))
            .andExpect(jsonPath("$.[*].dateEnvoi").value(hasItem(sameInstant(DEFAULT_DATE_ENVOI))));
    }

    @Test
    @Transactional
    void getEmail() throws Exception {
        // Initialize the database
        emailRepository.saveAndFlush(email);

        // Get the email
        restEmailMockMvc
            .perform(get(ENTITY_API_URL_ID, email.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(email.getId().intValue()))
            .andExpect(jsonPath("$.objet").value(DEFAULT_OBJET))
            .andExpect(jsonPath("$.contenu").value(DEFAULT_CONTENU))
            .andExpect(jsonPath("$.destinataire").value(DEFAULT_DESTINATAIRE))
            .andExpect(jsonPath("$.dateEnvoi").value(sameInstant(DEFAULT_DATE_ENVOI)));
    }

    @Test
    @Transactional
    void getNonExistingEmail() throws Exception {
        // Get the email
        restEmailMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmail() throws Exception {
        // Initialize the database
        emailRepository.saveAndFlush(email);

        int databaseSizeBeforeUpdate = emailRepository.findAll().size();

        // Update the email
        Email updatedEmail = emailRepository.findById(email.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEmail are not directly saved in db
        em.detach(updatedEmail);
        updatedEmail.objet(UPDATED_OBJET).contenu(UPDATED_CONTENU).destinataire(UPDATED_DESTINATAIRE).dateEnvoi(UPDATED_DATE_ENVOI);
        EmailDTO emailDTO = emailMapper.toDto(updatedEmail);

        restEmailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, emailDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(emailDTO))
            )
            .andExpect(status().isOk());

        // Validate the Email in the database
        List<Email> emailList = emailRepository.findAll();
        assertThat(emailList).hasSize(databaseSizeBeforeUpdate);
        Email testEmail = emailList.get(emailList.size() - 1);
        assertThat(testEmail.getObjet()).isEqualTo(UPDATED_OBJET);
        assertThat(testEmail.getContenu()).isEqualTo(UPDATED_CONTENU);
        assertThat(testEmail.getDestinataire()).isEqualTo(UPDATED_DESTINATAIRE);
        assertThat(testEmail.getDateEnvoi()).isEqualTo(UPDATED_DATE_ENVOI);
    }

    @Test
    @Transactional
    void putNonExistingEmail() throws Exception {
        int databaseSizeBeforeUpdate = emailRepository.findAll().size();
        email.setId(longCount.incrementAndGet());

        // Create the Email
        EmailDTO emailDTO = emailMapper.toDto(email);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, emailDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(emailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Email in the database
        List<Email> emailList = emailRepository.findAll();
        assertThat(emailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmail() throws Exception {
        int databaseSizeBeforeUpdate = emailRepository.findAll().size();
        email.setId(longCount.incrementAndGet());

        // Create the Email
        EmailDTO emailDTO = emailMapper.toDto(email);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(emailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Email in the database
        List<Email> emailList = emailRepository.findAll();
        assertThat(emailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmail() throws Exception {
        int databaseSizeBeforeUpdate = emailRepository.findAll().size();
        email.setId(longCount.incrementAndGet());

        // Create the Email
        EmailDTO emailDTO = emailMapper.toDto(email);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(emailDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Email in the database
        List<Email> emailList = emailRepository.findAll();
        assertThat(emailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmailWithPatch() throws Exception {
        // Initialize the database
        emailRepository.saveAndFlush(email);

        int databaseSizeBeforeUpdate = emailRepository.findAll().size();

        // Update the email using partial update
        Email partialUpdatedEmail = new Email();
        partialUpdatedEmail.setId(email.getId());

        partialUpdatedEmail.objet(UPDATED_OBJET);

        restEmailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmail.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmail))
            )
            .andExpect(status().isOk());

        // Validate the Email in the database
        List<Email> emailList = emailRepository.findAll();
        assertThat(emailList).hasSize(databaseSizeBeforeUpdate);
        Email testEmail = emailList.get(emailList.size() - 1);
        assertThat(testEmail.getObjet()).isEqualTo(UPDATED_OBJET);
        assertThat(testEmail.getContenu()).isEqualTo(DEFAULT_CONTENU);
        assertThat(testEmail.getDestinataire()).isEqualTo(DEFAULT_DESTINATAIRE);
        assertThat(testEmail.getDateEnvoi()).isEqualTo(DEFAULT_DATE_ENVOI);
    }

    @Test
    @Transactional
    void fullUpdateEmailWithPatch() throws Exception {
        // Initialize the database
        emailRepository.saveAndFlush(email);

        int databaseSizeBeforeUpdate = emailRepository.findAll().size();

        // Update the email using partial update
        Email partialUpdatedEmail = new Email();
        partialUpdatedEmail.setId(email.getId());

        partialUpdatedEmail.objet(UPDATED_OBJET).contenu(UPDATED_CONTENU).destinataire(UPDATED_DESTINATAIRE).dateEnvoi(UPDATED_DATE_ENVOI);

        restEmailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmail.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmail))
            )
            .andExpect(status().isOk());

        // Validate the Email in the database
        List<Email> emailList = emailRepository.findAll();
        assertThat(emailList).hasSize(databaseSizeBeforeUpdate);
        Email testEmail = emailList.get(emailList.size() - 1);
        assertThat(testEmail.getObjet()).isEqualTo(UPDATED_OBJET);
        assertThat(testEmail.getContenu()).isEqualTo(UPDATED_CONTENU);
        assertThat(testEmail.getDestinataire()).isEqualTo(UPDATED_DESTINATAIRE);
        assertThat(testEmail.getDateEnvoi()).isEqualTo(UPDATED_DATE_ENVOI);
    }

    @Test
    @Transactional
    void patchNonExistingEmail() throws Exception {
        int databaseSizeBeforeUpdate = emailRepository.findAll().size();
        email.setId(longCount.incrementAndGet());

        // Create the Email
        EmailDTO emailDTO = emailMapper.toDto(email);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, emailDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(emailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Email in the database
        List<Email> emailList = emailRepository.findAll();
        assertThat(emailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmail() throws Exception {
        int databaseSizeBeforeUpdate = emailRepository.findAll().size();
        email.setId(longCount.incrementAndGet());

        // Create the Email
        EmailDTO emailDTO = emailMapper.toDto(email);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(emailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Email in the database
        List<Email> emailList = emailRepository.findAll();
        assertThat(emailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmail() throws Exception {
        int databaseSizeBeforeUpdate = emailRepository.findAll().size();
        email.setId(longCount.incrementAndGet());

        // Create the Email
        EmailDTO emailDTO = emailMapper.toDto(email);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(emailDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Email in the database
        List<Email> emailList = emailRepository.findAll();
        assertThat(emailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmail() throws Exception {
        // Initialize the database
        emailRepository.saveAndFlush(email);

        int databaseSizeBeforeDelete = emailRepository.findAll().size();

        // Delete the email
        restEmailMockMvc
            .perform(delete(ENTITY_API_URL_ID, email.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Email> emailList = emailRepository.findAll();
        assertThat(emailList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
