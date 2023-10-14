import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Technician from './technician';
import TechnicianDetail from './technician-detail';
import TechnicianUpdate from './technician-update';
import TechnicianDeleteDialog from './technician-delete-dialog';

const TechnicianRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Technician />} />
    <Route path="new" element={<TechnicianUpdate />} />
    <Route path=":id">
      <Route index element={<TechnicianDetail />} />
      <Route path="edit" element={<TechnicianUpdate />} />
      <Route path="delete" element={<TechnicianDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TechnicianRoutes;
