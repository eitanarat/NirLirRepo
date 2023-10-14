import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './technician.reducer';

export const TechnicianDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const technicianEntity = useAppSelector(state => state.technician.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="technicianDetailsHeading">
          <Translate contentKey="nirLirApp.technician.detail.title">Technician</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{technicianEntity.id}</dd>
          <dt>
            <span id="userId">
              <Translate contentKey="nirLirApp.technician.userId">User Id</Translate>
            </span>
          </dt>
          <dd>{technicianEntity.userId}</dd>
          <dt>
            <span id="firstName">
              <Translate contentKey="nirLirApp.technician.firstName">First Name</Translate>
            </span>
          </dt>
          <dd>{technicianEntity.firstName}</dd>
          <dt>
            <span id="lastName">
              <Translate contentKey="nirLirApp.technician.lastName">Last Name</Translate>
            </span>
          </dt>
          <dd>{technicianEntity.lastName}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="nirLirApp.technician.email">Email</Translate>
            </span>
          </dt>
          <dd>{technicianEntity.email}</dd>
          <dt>
            <span id="mobileNumber">
              <Translate contentKey="nirLirApp.technician.mobileNumber">Mobile Number</Translate>
            </span>
          </dt>
          <dd>{technicianEntity.mobileNumber}</dd>
          <dt>
            <span id="langKey">
              <Translate contentKey="nirLirApp.technician.langKey">Lang Key</Translate>
            </span>
          </dt>
          <dd>{technicianEntity.langKey}</dd>
          <dt>
            <span id="birthdate">
              <Translate contentKey="nirLirApp.technician.birthdate">Birthdate</Translate>
            </span>
          </dt>
          <dd>
            {technicianEntity.birthdate ? <TextFormat value={technicianEntity.birthdate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
        </dl>
        <Button tag={Link} to="/technician" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/technician/${technicianEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TechnicianDetail;
