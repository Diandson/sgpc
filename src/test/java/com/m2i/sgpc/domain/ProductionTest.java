package com.m2i.sgpc.domain;

import static com.m2i.sgpc.domain.PersonneTestSamples.*;
import static com.m2i.sgpc.domain.ProductionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.m2i.sgpc.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Production.class);
        Production production1 = getProductionSample1();
        Production production2 = new Production();
        assertThat(production1).isNotEqualTo(production2);

        production2.setId(production1.getId());
        assertThat(production1).isEqualTo(production2);

        production2 = getProductionSample2();
        assertThat(production1).isNotEqualTo(production2);
    }

    @Test
    void personneTest() throws Exception {
        Production production = getProductionRandomSampleGenerator();
        Personne personneBack = getPersonneRandomSampleGenerator();

        production.setPersonne(personneBack);
        assertThat(production.getPersonne()).isEqualTo(personneBack);

        production.personne(null);
        assertThat(production.getPersonne()).isNull();
    }

    @Test
    void producteurTest() throws Exception {
        Production production = getProductionRandomSampleGenerator();
        Personne personneBack = getPersonneRandomSampleGenerator();

        production.setProducteur(personneBack);
        assertThat(production.getProducteur()).isEqualTo(personneBack);

        production.producteur(null);
        assertThat(production.getProducteur()).isNull();
    }

    @Test
    void receveurTest() throws Exception {
        Production production = getProductionRandomSampleGenerator();
        Personne personneBack = getPersonneRandomSampleGenerator();

        production.setReceveur(personneBack);
        assertThat(production.getReceveur()).isEqualTo(personneBack);

        production.receveur(null);
        assertThat(production.getReceveur()).isNull();
    }
}
