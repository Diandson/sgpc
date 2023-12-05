package com.m2i.sgpc.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PersonneTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Personne getPersonneSample1() {
        return new Personne()
            .id(1L)
            .nom("nom1")
            .prenom("prenom1")
            .titre("titre1")
            .numeroDocument("numeroDocument1")
            .telephone("telephone1");
    }

    public static Personne getPersonneSample2() {
        return new Personne()
            .id(2L)
            .nom("nom2")
            .prenom("prenom2")
            .titre("titre2")
            .numeroDocument("numeroDocument2")
            .telephone("telephone2");
    }

    public static Personne getPersonneRandomSampleGenerator() {
        return new Personne()
            .id(longCount.incrementAndGet())
            .nom(UUID.randomUUID().toString())
            .prenom(UUID.randomUUID().toString())
            .titre(UUID.randomUUID().toString())
            .numeroDocument(UUID.randomUUID().toString())
            .telephone(UUID.randomUUID().toString());
    }
}
