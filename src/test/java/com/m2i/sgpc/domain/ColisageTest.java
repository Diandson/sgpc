package com.m2i.sgpc.domain;

import static com.m2i.sgpc.domain.ColisageTestSamples.*;
import static com.m2i.sgpc.domain.EmailTestSamples.*;
import static com.m2i.sgpc.domain.PersonneTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.m2i.sgpc.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ColisageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Colisage.class);
        Colisage colisage1 = getColisageSample1();
        Colisage colisage2 = new Colisage();
        assertThat(colisage1).isNotEqualTo(colisage2);

        colisage2.setId(colisage1.getId());
        assertThat(colisage1).isEqualTo(colisage2);

        colisage2 = getColisageSample2();
        assertThat(colisage1).isNotEqualTo(colisage2);
    }

    @Test
    void emailTest() throws Exception {
        Colisage colisage = getColisageRandomSampleGenerator();
        Email emailBack = getEmailRandomSampleGenerator();

        colisage.addEmail(emailBack);
        assertThat(colisage.getEmails()).containsOnly(emailBack);
        assertThat(emailBack.getColisage()).isEqualTo(colisage);

        colisage.removeEmail(emailBack);
        assertThat(colisage.getEmails()).doesNotContain(emailBack);
        assertThat(emailBack.getColisage()).isNull();

        colisage.emails(new HashSet<>(Set.of(emailBack)));
        assertThat(colisage.getEmails()).containsOnly(emailBack);
        assertThat(emailBack.getColisage()).isEqualTo(colisage);

        colisage.setEmails(new HashSet<>());
        assertThat(colisage.getEmails()).doesNotContain(emailBack);
        assertThat(emailBack.getColisage()).isNull();
    }

    @Test
    void personneTest() throws Exception {
        Colisage colisage = getColisageRandomSampleGenerator();
        Personne personneBack = getPersonneRandomSampleGenerator();

        colisage.setPersonne(personneBack);
        assertThat(colisage.getPersonne()).isEqualTo(personneBack);

        colisage.personne(null);
        assertThat(colisage.getPersonne()).isNull();
    }
}
