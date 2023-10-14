package com.nirlir.service.mapper;

import com.nirlir.domain.Customer;
import com.nirlir.domain.ServiceRequest;
import com.nirlir.domain.Technician;
import com.nirlir.service.dto.CustomerDTO;
import com.nirlir.service.dto.ServiceRequestDTO;
import com.nirlir.service.dto.TechnicianDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ServiceRequest} and its DTO {@link ServiceRequestDTO}.
 */
@Mapper(componentModel = "spring")
public interface ServiceRequestMapper extends EntityMapper<ServiceRequestDTO, ServiceRequest> {
    @Mapping(target = "technicians", source = "technicians", qualifiedByName = "technicianIdSet")
    @Mapping(target = "customers", source = "customers", qualifiedByName = "customerIdSet")
    ServiceRequestDTO toDto(ServiceRequest s);

    @Mapping(target = "removeTechnician", ignore = true)
    @Mapping(target = "removeCustomer", ignore = true)
    ServiceRequest toEntity(ServiceRequestDTO serviceRequestDTO);

    @Named("technicianId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TechnicianDTO toDtoTechnicianId(Technician technician);

    @Named("technicianIdSet")
    default Set<TechnicianDTO> toDtoTechnicianIdSet(Set<Technician> technician) {
        return technician.stream().map(this::toDtoTechnicianId).collect(Collectors.toSet());
    }

    @Named("customerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomerDTO toDtoCustomerId(Customer customer);

    @Named("customerIdSet")
    default Set<CustomerDTO> toDtoCustomerIdSet(Set<Customer> customer) {
        return customer.stream().map(this::toDtoCustomerId).collect(Collectors.toSet());
    }
}
