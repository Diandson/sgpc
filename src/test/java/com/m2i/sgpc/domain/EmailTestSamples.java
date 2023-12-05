package com.m2i.sgpc.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EmailTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Email getEmailSample1() {
        return new Email().id(1L).objet("objet1").contenu("contenu1").destinataire("destinataire1");
    }

    public static Email getEmailSample2() {
        return new Email().id(2L).objet("objet2").contenu("contenu2").destinataire("destinataire2");
    }

    public static Email getEmailRandomSampleGenerator() {
        return new Email()
            .id(longCount.incrementAndGet())
            .objet(UUID.randomUUID().toString())
            .contenu(UUID.randomUUID().toString())
            .destinataire(UUID.randomUUID().toString());
    }
}
