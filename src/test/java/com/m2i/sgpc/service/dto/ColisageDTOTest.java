package com.m2i.sgpc.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.m2i.sgpc.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ColisageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ColisageDTO.class);
        ColisageDTO colisageDTO1 = new ColisageDTO();
        colisageDTO1.setId(1L);
        ColisageDTO colisageDTO2 = new ColisageDTO();
        assertThat(colisageDTO1).isNotEqualTo(colisageDTO2);
        colisageDTO2.setId(colisageDTO1.getId());
        assertThat(colisageDTO1).isEqualTo(colisageDTO2);
        colisageDTO2.setId(2L);
        assertThat(colisageDTO1).isNotEqualTo(colisageDTO2);
        colisageDTO1.setId(null);
        assertThat(colisageDTO1).isNotEqualTo(colisageDTO2);
    }
}
