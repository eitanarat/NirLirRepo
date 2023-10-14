import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './customer.reducer';

export const CustomerDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const customerEntity = useAppSelector(state => state.customer.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="customerDetailsHeading">
          <Translate contentKey="nirLirApp.customer.detail.title">Customer</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{customerEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="nirLirApp.customer.name">Name</Translate>
            </span>
          </dt>
          <dd>{customerEntity.name}</dd>
          <dt>
            <span id="registrationNumber">
              <Translate contentKey="nirLirApp.customer.registrationNumber">Registration Number</Translate>
            </span>
          </dt>
          <dd>{customerEntity.registrationNumber}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="nirLirApp.customer.email">Email</Translate>
            </span>
          </dt>
          <dd>{customerEntity.email}</dd>
          <dt>
            <span id="phoneNumber">
              <Translate contentKey="nirLirApp.customer.phoneNumber">Phone Number</Translate>
            </span>
          </dt>
          <dd>{customerEntity.phoneNumber}</dd>
          <dt>
            <span id="communicationChannel">
              <Translate contentKey="nirLirApp.customer.communicationChannel">Communication Channel</Translate>
            </span>
          </dt>
          <dd>{customerEntity.communicationChannel}</dd>
          <dt>
            <span id="mainContactEmail">
              <Translate contentKey="nirLirApp.customer.mainContactEmail">Main Contact Email</Translate>
            </span>
          </dt>
          <dd>{customerEntity.mainContactEmail}</dd>
          <dt>
            <span id="logo">
              <Translate contentKey="nirLirApp.customer.logo">Logo</Translate>
            </span>
          </dt>
          <dd>
            {customerEntity.logo ? (
              <div>
                {customerEntity.logoContentType ? (
                  <a onClick={openFile(customerEntity.logoContentType, customerEntity.logo)}>
                    <img src={`data:${customerEntity.logoContentType};base64,${customerEntity.logo}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {customerEntity.logoContentType}, {byteSize(customerEntity.logo)}
                </span>
              </div>
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/customer" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/customer/${customerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CustomerDetail;
