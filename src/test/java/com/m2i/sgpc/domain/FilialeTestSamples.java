package com.m2i.sgpc.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FilialeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Filiale getFilialeSample1() {
        return new Filiale().id(1L).denomination("denomination1").sigle("sigle1");
    }

    public static Filiale getFilialeSample2() {
        return new Filiale().id(2L).denomination("denomination2").sigle("sigle2");
    }

    public static Filiale getFilialeRandomSampleGenerator() {
        return new Filiale().id(longCount.incrementAndGet()).denomination(UUID.randomUUID().toString()).sigle(UUID.randomUUID().toString());
    }
}
