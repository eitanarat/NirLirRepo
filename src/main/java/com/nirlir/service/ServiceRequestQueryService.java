package com.nirlir.service;

import com.nirlir.domain.*; // for static metamodels
import com.nirlir.domain.ServiceRequest;
import com.nirlir.repository.ServiceRequestRepository;
import com.nirlir.service.criteria.ServiceRequestCriteria;
import com.nirlir.service.dto.ServiceRequestDTO;
import com.nirlir.service.mapper.ServiceRequestMapper;
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
 * Service for executing complex queries for {@link ServiceRequest} entities in the database.
 * The main input is a {@link ServiceRequestCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ServiceRequestDTO} or a {@link Page} of {@link ServiceRequestDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ServiceRequestQueryService extends QueryService<ServiceRequest> {

    private final Logger log = LoggerFactory.getLogger(ServiceRequestQueryService.class);

    private final ServiceRequestRepository serviceRequestRepository;

    private final ServiceRequestMapper serviceRequestMapper;

    public ServiceRequestQueryService(ServiceRequestRepository serviceRequestRepository, ServiceRequestMapper serviceRequestMapper) {
        this.serviceRequestRepository = serviceRequestRepository;
        this.serviceRequestMapper = serviceRequestMapper;
    }

    /**
     * Return a {@link List} of {@link ServiceRequestDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ServiceRequestDTO> findByCriteria(ServiceRequestCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ServiceRequest> specification = createSpecification(criteria);
        return serviceRequestMapper.toDto(serviceRequestRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ServiceRequestDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ServiceRequestDTO> findByCriteria(ServiceRequestCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ServiceRequest> specification = createSpecification(criteria);
        return serviceRequestRepository.findAll(specification, page).map(serviceRequestMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ServiceRequestCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ServiceRequest> specification = createSpecification(criteria);
        return serviceRequestRepository.count(specification);
    }

    /**
     * Function to convert {@link ServiceRequestCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ServiceRequest> createSpecification(ServiceRequestCriteria criteria) {
        Specification<ServiceRequest> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ServiceRequest_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), ServiceRequest_.type));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), ServiceRequest_.description));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), ServiceRequest_.date));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), ServiceRequest_.status));
            }
            if (criteria.getTechnicianId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTechnicianId(),
                            root -> root.join(ServiceRequest_.technicians, JoinType.LEFT).get(Technician_.id)
                        )
                    );
            }
            if (criteria.getCustomerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCustomerId(),
                            root -> root.join(ServiceRequest_.customers, JoinType.LEFT).get(Customer_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
