package com.nirlir.repository;

import com.nirlir.domain.ServiceRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ServiceRequestRepositoryWithBagRelationshipsImpl implements ServiceRequestRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<ServiceRequest> fetchBagRelationships(Optional<ServiceRequest> serviceRequest) {
        return serviceRequest.map(this::fetchTechnicians).map(this::fetchCustomers);
    }

    @Override
    public Page<ServiceRequest> fetchBagRelationships(Page<ServiceRequest> serviceRequests) {
        return new PageImpl<>(
            fetchBagRelationships(serviceRequests.getContent()),
            serviceRequests.getPageable(),
            serviceRequests.getTotalElements()
        );
    }

    @Override
    public List<ServiceRequest> fetchBagRelationships(List<ServiceRequest> serviceRequests) {
        return Optional.of(serviceRequests).map(this::fetchTechnicians).map(this::fetchCustomers).orElse(Collections.emptyList());
    }

    ServiceRequest fetchTechnicians(ServiceRequest result) {
        return entityManager
            .createQuery(
                "select serviceRequest from ServiceRequest serviceRequest left join fetch serviceRequest.technicians where serviceRequest is :serviceRequest",
                ServiceRequest.class
            )
            .setParameter("serviceRequest", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<ServiceRequest> fetchTechnicians(List<ServiceRequest> serviceRequests) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, serviceRequests.size()).forEach(index -> order.put(serviceRequests.get(index).getId(), index));
        List<ServiceRequest> result = entityManager
            .createQuery(
                "select distinct serviceRequest from ServiceRequest serviceRequest left join fetch serviceRequest.technicians where serviceRequest in :serviceRequests",
                ServiceRequest.class
            )
            .setParameter("serviceRequests", serviceRequests)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    ServiceRequest fetchCustomers(ServiceRequest result) {
        return entityManager
            .createQuery(
                "select serviceRequest from ServiceRequest serviceRequest left join fetch serviceRequest.customers where serviceRequest is :serviceRequest",
                ServiceRequest.class
            )
            .setParameter("serviceRequest", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<ServiceRequest> fetchCustomers(List<ServiceRequest> serviceRequests) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, serviceRequests.size()).forEach(index -> order.put(serviceRequests.get(index).getId(), index));
        List<ServiceRequest> result = entityManager
            .createQuery(
                "select distinct serviceRequest from ServiceRequest serviceRequest left join fetch serviceRequest.customers where serviceRequest in :serviceRequests",
                ServiceRequest.class
            )
            .setParameter("serviceRequests", serviceRequests)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
