import { Component, OnInit } from '@angular/core';
import {Meeting} from "../_models/meeting";
import {MeetingService} from "../_services/meeting.service";
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {User} from "../_models/user";
import {UserService} from "../_services/user.service";
import {take} from "rxjs";
import {Participant} from "../_models/participant";
import {EmployeeMeeting} from "../_models/employeeMeeting";
import {Response} from "../_models/response";

@Component({
  selector: 'app-meetings-list-employee',
  templateUrl: './meetings-list-employee.component.html',
  styleUrls: ['./meetings-list-employee.component.css']
})
export class MeetingsListEmployeeComponent implements OnInit {

  page: number = 1;
  size: number = 5;
  total: number = 0;
  meetings: Meeting[] = [];
  date: Date = new Date();
  user: User = {} as User;
  employeeMeetings: EmployeeMeeting[] = [];
  YES: string = Response[Response.YES];
  NO: string = Response[Response.NO];

  constructor(private meetingService: MeetingService, private router: Router, private toastr: ToastrService, private userService: UserService) {
    this.userService.currentUser$.pipe(take(1)).subscribe({
      next: user => {
        if(user) {
          this.user = user;
        }
      }
    });
  }

  ngOnInit(): void {
    this.date = new Date(this.date.getFullYear() + '-' + (this.date.getMonth() + 1) + '-' + (this.date.getDate()))
    this.date.setHours(0);
    this.date.setMinutes(0);
    this.date.setSeconds(0);
    this.date.setMilliseconds(0);
    this.getByEmployee(this.user.id);
  }

  getByEmployee(employeeId: number) {
    this.meetingService.getByEmployee(employeeId).subscribe({
      next: (response: Meeting[]) => {
        this.meetings = response;
        this.employeeMeetings = [];
        this.meetings.forEach(m => {
          let employeeMeeting : EmployeeMeeting = {} as EmployeeMeeting;
          employeeMeeting.id = m.id;
          employeeMeeting.date = m.date;
          employeeMeeting.start = m.start;
          employeeMeeting.end = m.end;
          employeeMeeting.createdAt = m.createdAt;
          employeeMeeting.updatedAt = m.updatedAt;
          m.participants.forEach(p => {
            if(p.employee.id === this.user.id) {
              employeeMeeting.response = p.response;
            }
          })
          this.employeeMeetings.push(employeeMeeting)
        })
        this.total = this.employeeMeetings.length;
      },
      error: err => {
        this.toastr.error(err.error);
      }
    })
  }

}
