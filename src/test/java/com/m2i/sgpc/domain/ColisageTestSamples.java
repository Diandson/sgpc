package com.m2i.sgpc.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ColisageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Colisage getColisageSample1() {
        return new Colisage().id(1L).destination("destination1").canal("canal1").recuPar("recuPar1");
    }

    public static Colisage getColisageSample2() {
        return new Colisage().id(2L).destination("destination2").canal("canal2").recuPar("recuPar2");
    }

    public static Colisage getColisageRandomSampleGenerator() {
        return new Colisage()
            .id(longCount.incrementAndGet())
            .destination(UUID.randomUUID().toString())
            .canal(UUID.randomUUID().toString())
            .recuPar(UUID.randomUUID().toString());
    }
}
