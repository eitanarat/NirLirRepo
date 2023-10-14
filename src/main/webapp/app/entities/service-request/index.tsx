import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ServiceRequest from './service-request';
import ServiceRequestDetail from './service-request-detail';
import ServiceRequestUpdate from './service-request-update';
import ServiceRequestDeleteDialog from './service-request-delete-dialog';

const ServiceRequestRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ServiceRequest />} />
    <Route path="new" element={<ServiceRequestUpdate />} />
    <Route path=":id">
      <Route index element={<ServiceRequestDetail />} />
      <Route path="edit" element={<ServiceRequestUpdate />} />
      <Route path="delete" element={<ServiceRequestDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ServiceRequestRoutes;
