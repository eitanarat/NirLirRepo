package com.nirlir.repository;

import com.nirlir.domain.Technician;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Technician entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TechnicianRepository extends JpaRepository<Technician, Long>, JpaSpecificationExecutor<Technician> {}
