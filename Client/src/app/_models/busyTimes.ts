import {Employee} from "./employee";

export interface BusyTime {

  id: string;
  date: Date;
  start: Date;
  end: Date;
  employee: Employee;
  description: string;

}
