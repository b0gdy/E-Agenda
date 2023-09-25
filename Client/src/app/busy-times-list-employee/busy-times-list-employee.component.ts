import { Component, OnInit } from '@angular/core';
import {Meeting} from "../_models/meeting";
import {User} from "../_models/user";
import {EmployeeMeeting} from "../_models/employeeMeeting";
import {Response} from "../_models/response";
import {MeetingService} from "../_services/meeting.service";
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {UserService} from "../_services/user.service";
import {take} from "rxjs";
import {BusyTime} from "../_models/busyTimes";
import {BusyTimeService} from "../_services/busy-time.service";

@Component({
  selector: 'app-busy-times-list-employee',
  templateUrl: './busy-times-list-employee.component.html',
  styleUrls: ['./busy-times-list-employee.component.css']
})
export class BusyTimesListEmployeeComponent implements OnInit {

  page: number = 1;
  size: number = 5;
  total: number = 0;
  busyTimes: BusyTime[] = [];
  date: Date = new Date();
  user: User = {} as User;
  fetched: boolean = false;

  constructor(private busyTimeService: BusyTimeService, private router: Router, private toastr: ToastrService, private userService: UserService) {
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
    this.fetched = false;
    this.getByEmployee(this.user.id);
  }

  getByEmployee(employeeId: number) {
    this.busyTimeService.getByEmployee(employeeId).subscribe({
      next: (response: BusyTime[]) => {
        this.busyTimes = response;
        this.fetched = true;
        this.total = this.busyTimes.length;
      },
      error: err => {
        this.toastr.error(err.error);
      }
    })
  }

}
