package com.nirlir.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nirlir.domain.enumeration.ServiceRequestStatus;
import com.nirlir.domain.enumeration.ServiceRequestType;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ServiceRequest.
 */
@Entity
@Table(name = "service_request")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServiceRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ServiceRequestType type;

    @Size(min = 3, max = 2000)
    @Column(name = "description", length = 2000)
    private String description;

    @NotNull
    @Column(name = "date", nullable = false)
    private ZonedDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ServiceRequestStatus status;

    @ManyToMany
    @JoinTable(
        name = "rel_service_request__technician",
        joinColumns = @JoinColumn(name = "service_request_id"),
        inverseJoinColumns = @JoinColumn(name = "technician_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "serviceRequests" }, allowSetters = true)
    private Set<Technician> technicians = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_service_request__customer",
        joinColumns = @JoinColumn(name = "service_request_id"),
        inverseJoinColumns = @JoinColumn(name = "customer_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "addresses", "serviceRequests" }, allowSetters = true)
    private Set<Customer> customers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ServiceRequest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ServiceRequestType getType() {
        return this.type;
    }

    public ServiceRequest type(ServiceRequestType type) {
        this.setType(type);
        return this;
    }

    public void setType(ServiceRequestType type) {
        this.type = type;
    }

    public String getDescription() {
        return this.description;
    }

    public ServiceRequest description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public ServiceRequest date(ZonedDateTime date) {
        this.setDate(date);
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public ServiceRequestStatus getStatus() {
        return this.status;
    }

    public ServiceRequest status(ServiceRequestStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ServiceRequestStatus status) {
        this.status = status;
    }

    public Set<Technician> getTechnicians() {
        return this.technicians;
    }

    public void setTechnicians(Set<Technician> technicians) {
        this.technicians = technicians;
    }

    public ServiceRequest technicians(Set<Technician> technicians) {
        this.setTechnicians(technicians);
        return this;
    }

    public ServiceRequest addTechnician(Technician technician) {
        this.technicians.add(technician);
        technician.getServiceRequests().add(this);
        return this;
    }

    public ServiceRequest removeTechnician(Technician technician) {
        this.technicians.remove(technician);
        technician.getServiceRequests().remove(this);
        return this;
    }

    public Set<Customer> getCustomers() {
        return this.customers;
    }

    public void setCustomers(Set<Customer> customers) {
        this.customers = customers;
    }

    public ServiceRequest customers(Set<Customer> customers) {
        this.setCustomers(customers);
        return this;
    }

    public ServiceRequest addCustomer(Customer customer) {
        this.customers.add(customer);
        customer.getServiceRequests().add(this);
        return this;
    }

    public ServiceRequest removeCustomer(Customer customer) {
        this.customers.remove(customer);
        customer.getServiceRequests().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceRequest)) {
            return false;
        }
        return id != null && id.equals(((ServiceRequest) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceRequest{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", description='" + getDescription() + "'" +
            ", date='" + getDate() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
