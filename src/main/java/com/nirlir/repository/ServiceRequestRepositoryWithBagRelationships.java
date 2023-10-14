package com.nirlir.repository;

import com.nirlir.domain.ServiceRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ServiceRequestRepositoryWithBagRelationships {
    Optional<ServiceRequest> fetchBagRelationships(Optional<ServiceRequest> serviceRequest);

    List<ServiceRequest> fetchBagRelationships(List<ServiceRequest> serviceRequests);

    Page<ServiceRequest> fetchBagRelationships(Page<ServiceRequest> serviceRequests);
}
