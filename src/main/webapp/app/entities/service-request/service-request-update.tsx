import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITechnician } from 'app/shared/model/technician.model';
import { getEntities as getTechnicians } from 'app/entities/technician/technician.reducer';
import { ICustomer } from 'app/shared/model/customer.model';
import { getEntities as getCustomers } from 'app/entities/customer/customer.reducer';
import { IServiceRequest } from 'app/shared/model/service-request.model';
import { ServiceRequestType } from 'app/shared/model/enumerations/service-request-type.model';
import { ServiceRequestStatus } from 'app/shared/model/enumerations/service-request-status.model';
import { getEntity, updateEntity, createEntity, reset } from './service-request.reducer';

export const ServiceRequestUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const technicians = useAppSelector(state => state.technician.entities);
  const customers = useAppSelector(state => state.customer.entities);
  const serviceRequestEntity = useAppSelector(state => state.serviceRequest.entity);
  const loading = useAppSelector(state => state.serviceRequest.loading);
  const updating = useAppSelector(state => state.serviceRequest.updating);
  const updateSuccess = useAppSelector(state => state.serviceRequest.updateSuccess);
  const serviceRequestTypeValues = Object.keys(ServiceRequestType);
  const serviceRequestStatusValues = Object.keys(ServiceRequestStatus);

  const handleClose = () => {
    navigate('/service-request' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTechnicians({}));
    dispatch(getCustomers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.date = convertDateTimeToServer(values.date);

    const entity = {
      ...serviceRequestEntity,
      ...values,
      technicians: mapIdList(values.technicians),
      customers: mapIdList(values.customers),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          date: displayDefaultDateTime(),
        }
      : {
          type: 'INSTALLATION',
          status: 'SCHEDULED',
          ...serviceRequestEntity,
          date: convertDateTimeFromServer(serviceRequestEntity.date),
          technicians: serviceRequestEntity?.technicians?.map(e => e.id.toString()),
          customers: serviceRequestEntity?.customers?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="nirLirApp.serviceRequest.home.createOrEditLabel" data-cy="ServiceRequestCreateUpdateHeading">
            <Translate contentKey="nirLirApp.serviceRequest.home.createOrEditLabel">Create or edit a ServiceRequest</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="service-request-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('nirLirApp.serviceRequest.type')}
                id="service-request-type"
                name="type"
                data-cy="type"
                type="select"
              >
                {serviceRequestTypeValues.map(serviceRequestType => (
                  <option value={serviceRequestType} key={serviceRequestType}>
                    {translate('nirLirApp.ServiceRequestType.' + serviceRequestType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('nirLirApp.serviceRequest.description')}
                id="service-request-description"
                name="description"
                data-cy="description"
                type="text"
                validate={{
                  minLength: { value: 3, message: translate('entity.validation.minlength', { min: 3 }) },
                  maxLength: { value: 2000, message: translate('entity.validation.maxlength', { max: 2000 }) },
                }}
              />
              <ValidatedField
                label={translate('nirLirApp.serviceRequest.date')}
                id="service-request-date"
                name="date"
                data-cy="date"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('nirLirApp.serviceRequest.status')}
                id="service-request-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {serviceRequestStatusValues.map(serviceRequestStatus => (
                  <option value={serviceRequestStatus} key={serviceRequestStatus}>
                    {translate('nirLirApp.ServiceRequestStatus.' + serviceRequestStatus)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('nirLirApp.serviceRequest.technician')}
                id="service-request-technician"
                data-cy="technician"
                type="select"
                multiple
                name="technicians"
              >
                <option value="" key="0" />
                {technicians
                  ? technicians.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('nirLirApp.serviceRequest.customer')}
                id="service-request-customer"
                data-cy="customer"
                type="select"
                multiple
                name="customers"
              >
                <option value="" key="0" />
                {customers
                  ? customers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/service-request" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ServiceRequestUpdate;
