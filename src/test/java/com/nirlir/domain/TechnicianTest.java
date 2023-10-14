package com.nirlir.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.nirlir.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TechnicianTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Technician.class);
        Technician technician1 = new Technician();
        technician1.setId(1L);
        Technician technician2 = new Technician();
        technician2.setId(technician1.getId());
        assertThat(technician1).isEqualTo(technician2);
        technician2.setId(2L);
        assertThat(technician1).isNotEqualTo(technician2);
        technician1.setId(null);
        assertThat(technician1).isNotEqualTo(technician2);
    }
}
