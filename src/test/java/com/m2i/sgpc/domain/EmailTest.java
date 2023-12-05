package com.m2i.sgpc.domain;

import static com.m2i.sgpc.domain.ColisageTestSamples.*;
import static com.m2i.sgpc.domain.EmailTestSamples.*;
import static com.m2i.sgpc.domain.PersonneTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.m2i.sgpc.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmailTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Email.class);
        Email email1 = getEmailSample1();
        Email email2 = new Email();
        assertThat(email1).isNotEqualTo(email2);

        email2.setId(email1.getId());
        assertThat(email1).isEqualTo(email2);

        email2 = getEmailSample2();
        assertThat(email1).isNotEqualTo(email2);
    }

    @Test
    void colisageTest() throws Exception {
        Email email = getEmailRandomSampleGenerator();
        Colisage colisageBack = getColisageRandomSampleGenerator();

        email.setColisage(colisageBack);
        assertThat(email.getColisage()).isEqualTo(colisageBack);

        email.colisage(null);
        assertThat(email.getColisage()).isNull();
    }

    @Test
    void personneTest() throws Exception {
        Email email = getEmailRandomSampleGenerator();
        Personne personneBack = getPersonneRandomSampleGenerator();

        email.setPersonne(personneBack);
        assertThat(email.getPersonne()).isEqualTo(personneBack);

        email.personne(null);
        assertThat(email.getPersonne()).isNull();
    }
}
