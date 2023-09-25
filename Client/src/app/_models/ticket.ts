import {Description} from "./description";
import {Employee} from "./employee";
import {Status} from "./status";
import {Priority} from "./priority";
import {Client} from "./client";

export interface Ticket {

  id: string;
  title: string;
  description: Description;
  createdAt: Date;
  updatedAt: Date;
  employee: Employee;
  client: Client;
  status: Status;
  priority: Priority;
  enabled: boolean;

}
