package com.m2i.sgpc.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.m2i.sgpc.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StockageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockageDTO.class);
        StockageDTO stockageDTO1 = new StockageDTO();
        stockageDTO1.setId(1L);
        StockageDTO stockageDTO2 = new StockageDTO();
        assertThat(stockageDTO1).isNotEqualTo(stockageDTO2);
        stockageDTO2.setId(stockageDTO1.getId());
        assertThat(stockageDTO1).isEqualTo(stockageDTO2);
        stockageDTO2.setId(2L);
        assertThat(stockageDTO1).isNotEqualTo(stockageDTO2);
        stockageDTO1.setId(null);
        assertThat(stockageDTO1).isNotEqualTo(stockageDTO2);
    }
}
