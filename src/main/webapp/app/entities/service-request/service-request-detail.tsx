import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './service-request.reducer';

export const ServiceRequestDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const serviceRequestEntity = useAppSelector(state => state.serviceRequest.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="serviceRequestDetailsHeading">
          <Translate contentKey="nirLirApp.serviceRequest.detail.title">ServiceRequest</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{serviceRequestEntity.id}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="nirLirApp.serviceRequest.type">Type</Translate>
            </span>
          </dt>
          <dd>{serviceRequestEntity.type}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="nirLirApp.serviceRequest.description">Description</Translate>
            </span>
          </dt>
          <dd>{serviceRequestEntity.description}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="nirLirApp.serviceRequest.date">Date</Translate>
            </span>
          </dt>
          <dd>
            {serviceRequestEntity.date ? <TextFormat value={serviceRequestEntity.date} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="status">
              <Translate contentKey="nirLirApp.serviceRequest.status">Status</Translate>
            </span>
          </dt>
          <dd>{serviceRequestEntity.status}</dd>
          <dt>
            <Translate contentKey="nirLirApp.serviceRequest.technician">Technician</Translate>
          </dt>
          <dd>
            {serviceRequestEntity.technicians
              ? serviceRequestEntity.technicians.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {serviceRequestEntity.technicians && i === serviceRequestEntity.technicians.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="nirLirApp.serviceRequest.customer">Customer</Translate>
          </dt>
          <dd>
            {serviceRequestEntity.customers
              ? serviceRequestEntity.customers.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {serviceRequestEntity.customers && i === serviceRequestEntity.customers.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/service-request" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/service-request/${serviceRequestEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ServiceRequestDetail;
