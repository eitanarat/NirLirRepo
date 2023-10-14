package com.nirlir.service.dto;

import com.nirlir.domain.enumeration.ServiceRequestStatus;
import com.nirlir.domain.enumeration.ServiceRequestType;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.nirlir.domain.ServiceRequest} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServiceRequestDTO implements Serializable {

    private Long id;

    private ServiceRequestType type;

    @Size(min = 3, max = 2000)
    private String description;

    @NotNull
    private ZonedDateTime date;

    private ServiceRequestStatus status;

    private Set<TechnicianDTO> technicians = new HashSet<>();

    private Set<CustomerDTO> customers = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ServiceRequestType getType() {
        return type;
    }

    public void setType(ServiceRequestType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public ServiceRequestStatus getStatus() {
        return status;
    }

    public void setStatus(ServiceRequestStatus status) {
        this.status = status;
    }

    public Set<TechnicianDTO> getTechnicians() {
        return technicians;
    }

    public void setTechnicians(Set<TechnicianDTO> technicians) {
        this.technicians = technicians;
    }

    public Set<CustomerDTO> getCustomers() {
        return customers;
    }

    public void setCustomers(Set<CustomerDTO> customers) {
        this.customers = customers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceRequestDTO)) {
            return false;
        }

        ServiceRequestDTO serviceRequestDTO = (ServiceRequestDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, serviceRequestDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceRequestDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", description='" + getDescription() + "'" +
            ", date='" + getDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", technicians=" + getTechnicians() +
            ", customers=" + getCustomers() +
            "}";
    }
}
