package com.m2i.sgpc.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProductionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Production getProductionSample1() {
        return new Production().id(1L).libelle("libelle1").validerPar("validerPar1");
    }

    public static Production getProductionSample2() {
        return new Production().id(2L).libelle("libelle2").validerPar("validerPar2");
    }

    public static Production getProductionRandomSampleGenerator() {
        return new Production()
            .id(longCount.incrementAndGet())
            .libelle(UUID.randomUUID().toString())
            .validerPar(UUID.randomUUID().toString());
    }
}
