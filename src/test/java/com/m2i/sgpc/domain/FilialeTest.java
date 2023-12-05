package com.m2i.sgpc.domain;

import static com.m2i.sgpc.domain.FilialeTestSamples.*;
import static com.m2i.sgpc.domain.PersonneTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.m2i.sgpc.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class FilialeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Filiale.class);
        Filiale filiale1 = getFilialeSample1();
        Filiale filiale2 = new Filiale();
        assertThat(filiale1).isNotEqualTo(filiale2);

        filiale2.setId(filiale1.getId());
        assertThat(filiale1).isEqualTo(filiale2);

        filiale2 = getFilialeSample2();
        assertThat(filiale1).isNotEqualTo(filiale2);
    }

    @Test
    void personneTest() throws Exception {
        Filiale filiale = getFilialeRandomSampleGenerator();
        Personne personneBack = getPersonneRandomSampleGenerator();

        filiale.addPersonne(personneBack);
        assertThat(filiale.getPersonnes()).containsOnly(personneBack);
        assertThat(personneBack.getFiliale()).isEqualTo(filiale);

        filiale.removePersonne(personneBack);
        assertThat(filiale.getPersonnes()).doesNotContain(personneBack);
        assertThat(personneBack.getFiliale()).isNull();

        filiale.personnes(new HashSet<>(Set.of(personneBack)));
        assertThat(filiale.getPersonnes()).containsOnly(personneBack);
        assertThat(personneBack.getFiliale()).isEqualTo(filiale);

        filiale.setPersonnes(new HashSet<>());
        assertThat(filiale.getPersonnes()).doesNotContain(personneBack);
        assertThat(personneBack.getFiliale()).isNull();
    }
}
