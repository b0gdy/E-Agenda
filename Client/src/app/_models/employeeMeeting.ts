import {Response} from "./response";

export interface EmployeeMeeting {

  id: string;
  date: Date;
  start: Date;
  end: Date;
  createdAt: Date;
  updatedAt: Date;
  response: Response;

}

