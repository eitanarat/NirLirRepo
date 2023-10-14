package com.nirlir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nirlir.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TechnicianDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TechnicianDTO.class);
        TechnicianDTO technicianDTO1 = new TechnicianDTO();
        technicianDTO1.setId(1L);
        TechnicianDTO technicianDTO2 = new TechnicianDTO();
        assertThat(technicianDTO1).isNotEqualTo(technicianDTO2);
        technicianDTO2.setId(technicianDTO1.getId());
        assertThat(technicianDTO1).isEqualTo(technicianDTO2);
        technicianDTO2.setId(2L);
        assertThat(technicianDTO1).isNotEqualTo(technicianDTO2);
        technicianDTO1.setId(null);
        assertThat(technicianDTO1).isNotEqualTo(technicianDTO2);
    }
}
