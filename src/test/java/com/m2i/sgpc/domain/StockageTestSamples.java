package com.m2i.sgpc.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StockageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Stockage getStockageSample1() {
        return new Stockage().id(1L).denomination("denomination1").code("code1").quantite("quantite1").modifierPar("modifierPar1");
    }

    public static Stockage getStockageSample2() {
        return new Stockage().id(2L).denomination("denomination2").code("code2").quantite("quantite2").modifierPar("modifierPar2");
    }

    public static Stockage getStockageRandomSampleGenerator() {
        return new Stockage()
            .id(longCount.incrementAndGet())
            .denomination(UUID.randomUUID().toString())
            .code(UUID.randomUUID().toString())
            .quantite(UUID.randomUUID().toString())
            .modifierPar(UUID.randomUUID().toString());
    }
}
