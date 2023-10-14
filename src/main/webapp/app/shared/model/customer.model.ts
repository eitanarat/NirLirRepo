import { IAddress } from 'app/shared/model/address.model';
import { IServiceRequest } from 'app/shared/model/service-request.model';
import { CommunicationChannel } from 'app/shared/model/enumerations/communication-channel.model';

export interface ICustomer {
  id?: number;
  name?: string;
  registrationNumber?: string;
  email?: string;
  phoneNumber?: string;
  communicationChannel?: CommunicationChannel | null;
  mainContactEmail?: string;
  logoContentType?: string | null;
  logo?: string | null;
  addresses?: IAddress[] | null;
  serviceRequests?: IServiceRequest[] | null;
}

export const defaultValue: Readonly<ICustomer> = {};
