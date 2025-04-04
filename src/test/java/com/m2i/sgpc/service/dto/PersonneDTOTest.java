package com.m2i.sgpc.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.m2i.sgpc.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PersonneDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersonneDTO.class);
        PersonneDTO personneDTO1 = new PersonneDTO();
        personneDTO1.setId(1L);
        PersonneDTO personneDTO2 = new PersonneDTO();
        assertThat(personneDTO1).isNotEqualTo(personneDTO2);
        personneDTO2.setId(personneDTO1.getId());
        assertThat(personneDTO1).isEqualTo(personneDTO2);
        personneDTO2.setId(2L);
        assertThat(personneDTO1).isNotEqualTo(personneDTO2);
        personneDTO1.setId(null);
        assertThat(personneDTO1).isNotEqualTo(personneDTO2);
    }
}
