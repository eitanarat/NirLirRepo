package com.nirlir.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TechnicianMapperTest {

    private TechnicianMapper technicianMapper;

    @BeforeEach
    public void setUp() {
        technicianMapper = new TechnicianMapperImpl();
    }
}
