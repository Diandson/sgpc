package com.m2i.sgpc.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.m2i.sgpc.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FilialeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FilialeDTO.class);
        FilialeDTO filialeDTO1 = new FilialeDTO();
        filialeDTO1.setId(1L);
        FilialeDTO filialeDTO2 = new FilialeDTO();
        assertThat(filialeDTO1).isNotEqualTo(filialeDTO2);
        filialeDTO2.setId(filialeDTO1.getId());
        assertThat(filialeDTO1).isEqualTo(filialeDTO2);
        filialeDTO2.setId(2L);
        assertThat(filialeDTO1).isNotEqualTo(filialeDTO2);
        filialeDTO1.setId(null);
        assertThat(filialeDTO1).isNotEqualTo(filialeDTO2);
    }
}
