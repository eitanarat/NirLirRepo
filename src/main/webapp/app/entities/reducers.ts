import customer from 'app/entities/customer/customer.reducer';
import address from 'app/entities/address/address.reducer';
import technician from 'app/entities/technician/technician.reducer';
import serviceRequest from 'app/entities/service-request/service-request.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  customer,
  address,
  technician,
  serviceRequest,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
