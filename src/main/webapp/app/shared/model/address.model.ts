import { ICustomer } from 'app/shared/model/customer.model';

export interface IAddress {
  id?: number;
  street?: string | null;
  houseNumber?: number;
  city?: string;
  zipCode?: string | null;
  customer?: ICustomer | null;
}

export const defaultValue: Readonly<IAddress> = {};
