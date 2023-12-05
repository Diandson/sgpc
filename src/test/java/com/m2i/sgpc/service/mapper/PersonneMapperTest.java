package com.m2i.sgpc.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class PersonneMapperTest {

    private PersonneMapper personneMapper;

    @BeforeEach
    public void setUp() {
        personneMapper = new PersonneMapperImpl();
    }
}
