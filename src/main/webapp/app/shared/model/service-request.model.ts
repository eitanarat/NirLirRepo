import dayjs from 'dayjs';
import { ITechnician } from 'app/shared/model/technician.model';
import { ICustomer } from 'app/shared/model/customer.model';
import { ServiceRequestType } from 'app/shared/model/enumerations/service-request-type.model';
import { ServiceRequestStatus } from 'app/shared/model/enumerations/service-request-status.model';

export interface IServiceRequest {
  id?: number;
  type?: ServiceRequestType | null;
  description?: string | null;
  date?: string;
  status?: ServiceRequestStatus | null;
  technicians?: ITechnician[] | null;
  customers?: ICustomer[] | null;
}

export const defaultValue: Readonly<IServiceRequest> = {};
