import {Company} from "./company";

export interface Client {

  id: number;
  userName: string;
  enabled: boolean;
  role: string;
  firstName: string;
  lastName: string;
  company: Company;

}
