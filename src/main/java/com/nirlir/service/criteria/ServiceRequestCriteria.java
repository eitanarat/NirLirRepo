package com.nirlir.service.criteria;

import com.nirlir.domain.enumeration.ServiceRequestStatus;
import com.nirlir.domain.enumeration.ServiceRequestType;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nirlir.domain.ServiceRequest} entity. This class is used
 * in {@link com.nirlir.web.rest.ServiceRequestResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /service-requests?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServiceRequestCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ServiceRequestType
     */
    public static class ServiceRequestTypeFilter extends Filter<ServiceRequestType> {

        public ServiceRequestTypeFilter() {}

        public ServiceRequestTypeFilter(ServiceRequestTypeFilter filter) {
            super(filter);
        }

        @Override
        public ServiceRequestTypeFilter copy() {
            return new ServiceRequestTypeFilter(this);
        }
    }

    /**
     * Class for filtering ServiceRequestStatus
     */
    public static class ServiceRequestStatusFilter extends Filter<ServiceRequestStatus> {

        public ServiceRequestStatusFilter() {}

        public ServiceRequestStatusFilter(ServiceRequestStatusFilter filter) {
            super(filter);
        }

        @Override
        public ServiceRequestStatusFilter copy() {
            return new ServiceRequestStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ServiceRequestTypeFilter type;

    private StringFilter description;

    private ZonedDateTimeFilter date;

    private ServiceRequestStatusFilter status;

    private LongFilter technicianId;

    private LongFilter customerId;

    private Boolean distinct;

    public ServiceRequestCriteria() {}

    public ServiceRequestCriteria(ServiceRequestCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.technicianId = other.technicianId == null ? null : other.technicianId.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ServiceRequestCriteria copy() {
        return new ServiceRequestCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public ServiceRequestTypeFilter getType() {
        return type;
    }

    public ServiceRequestTypeFilter type() {
        if (type == null) {
            type = new ServiceRequestTypeFilter();
        }
        return type;
    }

    public void setType(ServiceRequestTypeFilter type) {
        this.type = type;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public ZonedDateTimeFilter getDate() {
        return date;
    }

    public ZonedDateTimeFilter date() {
        if (date == null) {
            date = new ZonedDateTimeFilter();
        }
        return date;
    }

    public void setDate(ZonedDateTimeFilter date) {
        this.date = date;
    }

    public ServiceRequestStatusFilter getStatus() {
        return status;
    }

    public ServiceRequestStatusFilter status() {
        if (status == null) {
            status = new ServiceRequestStatusFilter();
        }
        return status;
    }

    public void setStatus(ServiceRequestStatusFilter status) {
        this.status = status;
    }

    public LongFilter getTechnicianId() {
        return technicianId;
    }

    public LongFilter technicianId() {
        if (technicianId == null) {
            technicianId = new LongFilter();
        }
        return technicianId;
    }

    public void setTechnicianId(LongFilter technicianId) {
        this.technicianId = technicianId;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public LongFilter customerId() {
        if (customerId == null) {
            customerId = new LongFilter();
        }
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ServiceRequestCriteria that = (ServiceRequestCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(type, that.type) &&
            Objects.equals(description, that.description) &&
            Objects.equals(date, that.date) &&
            Objects.equals(status, that.status) &&
            Objects.equals(technicianId, that.technicianId) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, description, date, status, technicianId, customerId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceRequestCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (technicianId != null ? "technicianId=" + technicianId + ", " : "") +
            (customerId != null ? "customerId=" + customerId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
