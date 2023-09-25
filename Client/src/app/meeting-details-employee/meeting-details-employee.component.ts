import { Component, OnInit } from '@angular/core';
import {Meeting} from "../_models/meeting";
import {Participant} from "../_models/participant";
import {Response} from "../_models/response";
import {Employee} from "../_models/employee";
import {ActivatedRoute, Params} from "@angular/router";
import {MeetingService} from "../_services/meeting.service";
import {ToastrService} from "ngx-toastr";
import {UserService} from "../_services/user.service";
import {take} from "rxjs";
import {User} from "../_models/user";

@Component({
  selector: 'app-meeting-details-employee',
  templateUrl: './meeting-details-employee.component.html',
  styleUrls: ['./meeting-details-employee.component.css']
})
export class MeetingDetailsEmployeeComponent implements OnInit {

  meetingId: string = {} as string;
  meeting: Meeting = {} as Meeting;
  participants: Participant[] = [];
  participant: Participant = {} as Participant;
  response: Response = {} as Response;
  employee: Employee = {} as Employee
  YES: string = Response[Response.YES];
  NO: string = Response[Response.NO];
  user: User = {} as User;

  constructor(private route: ActivatedRoute, private meetingService: MeetingService, private toastr: ToastrService, private userService: UserService) {
    this.userService.currentUser$.pipe(take(1)).subscribe({
      next: user => {
        if(user) {
          this.user = user;
        }
      }
    });
  }

  ngOnInit(): void {
    this.route.params.subscribe({
      next: (params: Params) => {
        this.meetingId= params['id'];
        this.getById(this.meetingId);
      },
      error: err => {
        this.toastr.error(err.error);
      }
    })
  }

  getById(id: string) {
    this.meetingService.getById(id).subscribe({
      next: (response: Meeting) => {
        this.meeting = response;
      },
      error: err => {
        this.toastr.error(err.error);
      }
    })
  }

  addResponse(respone: Response){
    this.meetingService.addResponse(this.meetingId, respone, this.user.id).subscribe({
      next: (response: Meeting) => {
        this.meeting = response;
      },
      error: err => {
        this.toastr.error(err.error);
      },
      complete: () => {
        this.toastr.success("Response added!")
    }
    })
  }

  protected readonly Response = Response;
}
