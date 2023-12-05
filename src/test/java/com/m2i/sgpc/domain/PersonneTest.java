package com.m2i.sgpc.domain;

import static com.m2i.sgpc.domain.ColisageTestSamples.*;
import static com.m2i.sgpc.domain.EmailTestSamples.*;
import static com.m2i.sgpc.domain.FilialeTestSamples.*;
import static com.m2i.sgpc.domain.PersonneTestSamples.*;
import static com.m2i.sgpc.domain.ProductionTestSamples.*;
import static com.m2i.sgpc.domain.StockageTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.m2i.sgpc.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PersonneTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Personne.class);
        Personne personne1 = getPersonneSample1();
        Personne personne2 = new Personne();
        assertThat(personne1).isNotEqualTo(personne2);

        personne2.setId(personne1.getId());
        assertThat(personne1).isEqualTo(personne2);

        personne2 = getPersonneSample2();
        assertThat(personne1).isNotEqualTo(personne2);
    }

    @Test
    void productionTest() throws Exception {
        Personne personne = getPersonneRandomSampleGenerator();
        Production productionBack = getProductionRandomSampleGenerator();

        personne.addProduction(productionBack);
        assertThat(personne.getProductions()).containsOnly(productionBack);
        assertThat(productionBack.getPersonne()).isEqualTo(personne);

        personne.removeProduction(productionBack);
        assertThat(personne.getProductions()).doesNotContain(productionBack);
        assertThat(productionBack.getPersonne()).isNull();

        personne.productions(new HashSet<>(Set.of(productionBack)));
        assertThat(personne.getProductions()).containsOnly(productionBack);
        assertThat(productionBack.getPersonne()).isEqualTo(personne);

        personne.setProductions(new HashSet<>());
        assertThat(personne.getProductions()).doesNotContain(productionBack);
        assertThat(productionBack.getPersonne()).isNull();
    }

    @Test
    void stockageTest() throws Exception {
        Personne personne = getPersonneRandomSampleGenerator();
        Stockage stockageBack = getStockageRandomSampleGenerator();

        personne.addStockage(stockageBack);
        assertThat(personne.getStockages()).containsOnly(stockageBack);
        assertThat(stockageBack.getPersonne()).isEqualTo(personne);

        personne.removeStockage(stockageBack);
        assertThat(personne.getStockages()).doesNotContain(stockageBack);
        assertThat(stockageBack.getPersonne()).isNull();

        personne.stockages(new HashSet<>(Set.of(stockageBack)));
        assertThat(personne.getStockages()).containsOnly(stockageBack);
        assertThat(stockageBack.getPersonne()).isEqualTo(personne);

        personne.setStockages(new HashSet<>());
        assertThat(personne.getStockages()).doesNotContain(stockageBack);
        assertThat(stockageBack.getPersonne()).isNull();
    }

    @Test
    void colisageTest() throws Exception {
        Personne personne = getPersonneRandomSampleGenerator();
        Colisage colisageBack = getColisageRandomSampleGenerator();

        personne.addColisage(colisageBack);
        assertThat(personne.getColisages()).containsOnly(colisageBack);
        assertThat(colisageBack.getPersonne()).isEqualTo(personne);

        personne.removeColisage(colisageBack);
        assertThat(personne.getColisages()).doesNotContain(colisageBack);
        assertThat(colisageBack.getPersonne()).isNull();

        personne.colisages(new HashSet<>(Set.of(colisageBack)));
        assertThat(personne.getColisages()).containsOnly(colisageBack);
        assertThat(colisageBack.getPersonne()).isEqualTo(personne);

        personne.setColisages(new HashSet<>());
        assertThat(personne.getColisages()).doesNotContain(colisageBack);
        assertThat(colisageBack.getPersonne()).isNull();
    }

    @Test
    void emailTest() throws Exception {
        Personne personne = getPersonneRandomSampleGenerator();
        Email emailBack = getEmailRandomSampleGenerator();

        personne.addEmail(emailBack);
        assertThat(personne.getEmails()).containsOnly(emailBack);
        assertThat(emailBack.getPersonne()).isEqualTo(personne);

        personne.removeEmail(emailBack);
        assertThat(personne.getEmails()).doesNotContain(emailBack);
        assertThat(emailBack.getPersonne()).isNull();

        personne.emails(new HashSet<>(Set.of(emailBack)));
        assertThat(personne.getEmails()).containsOnly(emailBack);
        assertThat(emailBack.getPersonne()).isEqualTo(personne);

        personne.setEmails(new HashSet<>());
        assertThat(personne.getEmails()).doesNotContain(emailBack);
        assertThat(emailBack.getPersonne()).isNull();
    }

    @Test
    void filialeTest() throws Exception {
        Personne personne = getPersonneRandomSampleGenerator();
        Filiale filialeBack = getFilialeRandomSampleGenerator();

        personne.setFiliale(filialeBack);
        assertThat(personne.getFiliale()).isEqualTo(filialeBack);

        personne.filiale(null);
        assertThat(personne.getFiliale()).isNull();
    }

    @Test
    void producteurTest() throws Exception {
        Personne personne = getPersonneRandomSampleGenerator();
        Production productionBack = getProductionRandomSampleGenerator();

        personne.addProducteur(productionBack);
        assertThat(personne.getProducteurs()).containsOnly(productionBack);
        assertThat(productionBack.getProducteur()).isEqualTo(personne);

        personne.removeProducteur(productionBack);
        assertThat(personne.getProducteurs()).doesNotContain(productionBack);
        assertThat(productionBack.getProducteur()).isNull();

        personne.producteurs(new HashSet<>(Set.of(productionBack)));
        assertThat(personne.getProducteurs()).containsOnly(productionBack);
        assertThat(productionBack.getProducteur()).isEqualTo(personne);

        personne.setProducteurs(new HashSet<>());
        assertThat(personne.getProducteurs()).doesNotContain(productionBack);
        assertThat(productionBack.getProducteur()).isNull();
    }

    @Test
    void receveurTest() throws Exception {
        Personne personne = getPersonneRandomSampleGenerator();
        Production productionBack = getProductionRandomSampleGenerator();

        personne.addReceveur(productionBack);
        assertThat(personne.getReceveurs()).containsOnly(productionBack);
        assertThat(productionBack.getReceveur()).isEqualTo(personne);

        personne.removeReceveur(productionBack);
        assertThat(personne.getReceveurs()).doesNotContain(productionBack);
        assertThat(productionBack.getReceveur()).isNull();

        personne.receveurs(new HashSet<>(Set.of(productionBack)));
        assertThat(personne.getReceveurs()).containsOnly(productionBack);
        assertThat(productionBack.getReceveur()).isEqualTo(personne);

        personne.setReceveurs(new HashSet<>());
        assertThat(personne.getReceveurs()).doesNotContain(productionBack);
        assertThat(productionBack.getReceveur()).isNull();
    }
}
