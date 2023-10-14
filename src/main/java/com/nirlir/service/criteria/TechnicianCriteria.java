package com.nirlir.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nirlir.domain.Technician} entity. This class is used
 * in {@link com.nirlir.web.rest.TechnicianResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /technicians?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TechnicianCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter userId;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter email;

    private StringFilter mobileNumber;

    private StringFilter langKey;

    private ZonedDateTimeFilter birthdate;

    private LongFilter serviceRequestId;

    private Boolean distinct;

    public TechnicianCriteria() {}

    public TechnicianCriteria(TechnicianCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.mobileNumber = other.mobileNumber == null ? null : other.mobileNumber.copy();
        this.langKey = other.langKey == null ? null : other.langKey.copy();
        this.birthdate = other.birthdate == null ? null : other.birthdate.copy();
        this.serviceRequestId = other.serviceRequestId == null ? null : other.serviceRequestId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TechnicianCriteria copy() {
        return new TechnicianCriteria(this);
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

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public StringFilter firstName() {
        if (firstName == null) {
            firstName = new StringFilter();
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public StringFilter lastName() {
        if (lastName == null) {
            lastName = new StringFilter();
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
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

    public StringFilter getMobileNumber() {
        return mobileNumber;
    }

    public StringFilter mobileNumber() {
        if (mobileNumber == null) {
            mobileNumber = new StringFilter();
        }
        return mobileNumber;
    }

    public void setMobileNumber(StringFilter mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public StringFilter getLangKey() {
        return langKey;
    }

    public StringFilter langKey() {
        if (langKey == null) {
            langKey = new StringFilter();
        }
        return langKey;
    }

    public void setLangKey(StringFilter langKey) {
        this.langKey = langKey;
    }

    public ZonedDateTimeFilter getBirthdate() {
        return birthdate;
    }

    public ZonedDateTimeFilter birthdate() {
        if (birthdate == null) {
            birthdate = new ZonedDateTimeFilter();
        }
        return birthdate;
    }

    public void setBirthdate(ZonedDateTimeFilter birthdate) {
        this.birthdate = birthdate;
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
        final TechnicianCriteria that = (TechnicianCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(email, that.email) &&
            Objects.equals(mobileNumber, that.mobileNumber) &&
            Objects.equals(langKey, that.langKey) &&
            Objects.equals(birthdate, that.birthdate) &&
            Objects.equals(serviceRequestId, that.serviceRequestId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, firstName, lastName, email, mobileNumber, langKey, birthdate, serviceRequestId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TechnicianCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (firstName != null ? "firstName=" + firstName + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (mobileNumber != null ? "mobileNumber=" + mobileNumber + ", " : "") +
            (langKey != null ? "langKey=" + langKey + ", " : "") +
            (birthdate != null ? "birthdate=" + birthdate + ", " : "") +
            (serviceRequestId != null ? "serviceRequestId=" + serviceRequestId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
