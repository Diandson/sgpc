package com.m2i.sgpc.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class EmailMapperTest {

    private EmailMapper emailMapper;

    @BeforeEach
    public void setUp() {
        emailMapper = new EmailMapperImpl();
    }
}
