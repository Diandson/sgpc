package com.m2i.sgpc.domain;

import static com.m2i.sgpc.domain.PersonneTestSamples.*;
import static com.m2i.sgpc.domain.StockageTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.m2i.sgpc.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StockageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Stockage.class);
        Stockage stockage1 = getStockageSample1();
        Stockage stockage2 = new Stockage();
        assertThat(stockage1).isNotEqualTo(stockage2);

        stockage2.setId(stockage1.getId());
        assertThat(stockage1).isEqualTo(stockage2);

        stockage2 = getStockageSample2();
        assertThat(stockage1).isNotEqualTo(stockage2);
    }

    @Test
    void personneTest() throws Exception {
        Stockage stockage = getStockageRandomSampleGenerator();
        Personne personneBack = getPersonneRandomSampleGenerator();

        stockage.setPersonne(personneBack);
        assertThat(stockage.getPersonne()).isEqualTo(personneBack);

        stockage.personne(null);
        assertThat(stockage.getPersonne()).isNull();
    }
}
