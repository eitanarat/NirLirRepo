package com.nirlir.service.mapper;

import com.nirlir.domain.Technician;
import com.nirlir.service.dto.TechnicianDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Technician} and its DTO {@link TechnicianDTO}.
 */
@Mapper(componentModel = "spring")
public interface TechnicianMapper extends EntityMapper<TechnicianDTO, Technician> {}
