import {Participant} from "./participant";

export interface Meeting {

  id: string;
  date: Date;
  start: Date;
  end: Date;
  createdAt: Date;
  updatedAt: Date;
  participants: Participant[];

}

