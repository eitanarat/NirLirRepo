package com.nirlir.service;

import com.nirlir.domain.*; // for static metamodels
import com.nirlir.domain.Technician;
import com.nirlir.repository.TechnicianRepository;
import com.nirlir.service.criteria.TechnicianCriteria;
import com.nirlir.service.dto.TechnicianDTO;
import com.nirlir.service.mapper.TechnicianMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Technician} entities in the database.
 * The main input is a {@link TechnicianCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TechnicianDTO} or a {@link Page} of {@link TechnicianDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TechnicianQueryService extends QueryService<Technician> {

    private final Logger log = LoggerFactory.getLogger(TechnicianQueryService.class);

    private final TechnicianRepository technicianRepository;

    private final TechnicianMapper technicianMapper;

    public TechnicianQueryService(TechnicianRepository technicianRepository, TechnicianMapper technicianMapper) {
        this.technicianRepository = technicianRepository;
        this.technicianMapper = technicianMapper;
    }

    /**
     * Return a {@link List} of {@link TechnicianDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TechnicianDTO> findByCriteria(TechnicianCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Technician> specification = createSpecification(criteria);
        return technicianMapper.toDto(technicianRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TechnicianDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TechnicianDTO> findByCriteria(TechnicianCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Technician> specification = createSpecification(criteria);
        return technicianRepository.findAll(specification, page).map(technicianMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TechnicianCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Technician> specification = createSpecification(criteria);
        return technicianRepository.count(specification);
    }

    /**
     * Function to convert {@link TechnicianCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Technician> createSpecification(TechnicianCriteria criteria) {
        Specification<Technician> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Technician_.id));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserId(), Technician_.userId));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Technician_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Technician_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Technician_.email));
            }
            if (criteria.getMobileNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMobileNumber(), Technician_.mobileNumber));
            }
            if (criteria.getLangKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLangKey(), Technician_.langKey));
            }
            if (criteria.getBirthdate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBirthdate(), Technician_.birthdate));
            }
            if (criteria.getServiceRequestId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getServiceRequestId(),
                            root -> root.join(Technician_.serviceRequests, JoinType.LEFT).get(ServiceRequest_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
