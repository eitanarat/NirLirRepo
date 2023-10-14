import dayjs from 'dayjs';
import { IServiceRequest } from 'app/shared/model/service-request.model';

export interface ITechnician {
  id?: number;
  userId?: number | null;
  firstName?: string;
  lastName?: string;
  email?: string;
  mobileNumber?: string;
  langKey?: string;
  birthdate?: string;
  serviceRequests?: IServiceRequest[] | null;
}

export const defaultValue: Readonly<ITechnician> = {};
