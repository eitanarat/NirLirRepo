package com.nirlir.service.criteria;

import com.nirlir.domain.enumeration.CommunicationChannel;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nirlir.domain.Customer} entity. This class is used
 * in {@link com.nirlir.web.rest.CustomerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /customers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerCriteria implements Serializable, Criteria {

    /**
     * Class for filtering CommunicationChannel
     */
    public static class CommunicationChannelFilter extends Filter<CommunicationChannel> {

        public CommunicationChannelFilter() {}

        public CommunicationChannelFilter(CommunicationChannelFilter filter) {
            super(filter);
        }

        @Override
        public CommunicationChannelFilter copy() {
            return new CommunicationChannelFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter registrationNumber;

    private StringFilter email;

    private StringFilter phoneNumber;

    private CommunicationChannelFilter communicationChannel;

    private StringFilter mainContactEmail;

    private LongFilter addressId;

    private LongFilter serviceRequestId;

    private Boolean distinct;

    public CustomerCriteria() {}

    public CustomerCriteria(CustomerCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.registrationNumber = other.registrationNumber == null ? null : other.registrationNumber.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.phoneNumber = other.phoneNumber == null ? null : other.phoneNumber.copy();
        this.communicationChannel = other.communicationChannel == null ? null : other.communicationChannel.copy();
        this.mainContactEmail = other.mainContactEmail == null ? null : other.mainContactEmail.copy();
        this.addressId = other.addressId == null ? null : other.addressId.copy();
        this.serviceRequestId = other.serviceRequestId == null ? null : other.serviceRequestId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CustomerCriteria copy() {
        return new CustomerCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getRegistrationNumber() {
        return registrationNumber;
    }

    public StringFilter registrationNumber() {
        if (registrationNumber == null) {
            registrationNumber = new StringFilter();
        }
        return registrationNumber;
    }

    public void setRegistrationNumber(StringFilter registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public StringFilter phoneNumber() {
        if (phoneNumber == null) {
            phoneNumber = new StringFilter();
        }
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public CommunicationChannelFilter getCommunicationChannel() {
        return communicationChannel;
    }

    public CommunicationChannelFilter communicationChannel() {
        if (communicationChannel == null) {
            communicationChannel = new CommunicationChannelFilter();
        }
        return communicationChannel;
    }

    public void setCommunicationChannel(CommunicationChannelFilter communicationChannel) {
        this.communicationChannel = communicationChannel;
    }

    public StringFilter getMainContactEmail() {
        return mainContactEmail;
    }

    public StringFilter mainContactEmail() {
        if (mainContactEmail == null) {
            mainContactEmail = new StringFilter();
        }
        return mainContactEmail;
    }

    public void setMainContactEmail(StringFilter mainContactEmail) {
        this.mainContactEmail = mainContactEmail;
    }

    public LongFilter getAddressId() {
        return addressId;
    }

    public LongFilter addressId() {
        if (addressId == null) {
            addressId = new LongFilter();
        }
        return addressId;
    }

    public void setAddressId(LongFilter addressId) {
        this.addressId = addressId;
    }

    public LongFilter getServiceRequestId() {
        return serviceRequestId;
    }

    public LongFilter serviceRequestId() {
        if (serviceRequestId == null) {
            serviceRequestId = new LongFilter();
        }
        return serviceRequestId;
    }

    public void setServiceRequestId(LongFilter serviceRequestId) {
        this.serviceRequestId = serviceRequestId;
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
        final CustomerCriteria that = (CustomerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(registrationNumber, that.registrationNumber) &&
            Objects.equals(email, that.email) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(communicationChannel, that.communicationChannel) &&
            Objects.equals(mainContactEmail, that.mainContactEmail) &&
            Objects.equals(addressId, that.addressId) &&
            Objects.equals(serviceRequestId, that.serviceRequestId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            registrationNumber,
            email,
            phoneNumber,
            communicationChannel,
            mainContactEmail,
            addressId,
            serviceRequestId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (registrationNumber != null ? "registrationNumber=" + registrationNumber + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (phoneNumber != null ? "phoneNumber=" + phoneNumber + ", " : "") +
            (communicationChannel != null ? "communicationChannel=" + communicationChannel + ", " : "") +
            (mainContactEmail != null ? "mainContactEmail=" + mainContactEmail + ", " : "") +
            (addressId != null ? "addressId=" + addressId + ", " : "") +
            (serviceRequestId != null ? "serviceRequestId=" + serviceRequestId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
