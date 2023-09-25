import { Component, OnInit } from '@angular/core';
import {Meeting} from "../_models/meeting";
import {Participant} from "../_models/participant";
import {Response} from "../_models/response";
import {Employee} from "../_models/employee";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {MeetingService} from "../_services/meeting.service";
import {ToastrService} from "ngx-toastr";
import {BusyTime} from "../_models/busyTimes";
import {UserService} from "../_services/user.service";
import {take} from "rxjs";
import {BusyTimeService} from "../_services/busy-time.service";
import {User} from "../_models/user";

@Component({
  selector: 'app-busy-time-details-employee',
  templateUrl: './busy-time-details-employee.component.html',
  styleUrls: ['./busy-time-details-employee.component.css']
})
export class BusyTimeDetailsEmployeeComponent implements OnInit {

  busyTimeId: string = {} as string;
  busyTime: BusyTime = {} as BusyTime;
  response: Response = {} as Response;
  employee: Employee = {} as Employee
  fetched: boolean = false;
  user: User = {} as User;

  constructor(private route: ActivatedRoute, private router: Router, private busyTimeService: BusyTimeService, private toastr: ToastrService, private userService: UserService) {
    this.userService.currentUser$.pipe(take(1)).subscribe({
      next: user => {
        if(user) {
          this.user = user;
        }
      }
    });
  }


  ngOnInit(): void {
    this.fetched = false;
    this.route.params.subscribe({
      next: (params: Params) => {
        this.busyTimeId= params['id'];
        this.getById(this.busyTimeId);
      },
      error: err => {
        this.toastr.error(err.error);
      }
    })
  }

  getById(id: string) {
    this.busyTimeService.getById(id).subscribe({
      next: (response: BusyTime) => {
        this.busyTime = response;
        this.fetched = true;
      },
      error: err => {
        this.toastr.error(err.error);
      }
    })
  }

  delete(id: string) {
    this.busyTimeService.delete(id).subscribe({
      next: (response: string) => {
        // this.toastr.success(response);
        this.router.navigateByUrl('/busy-times-list-employee');
      },
      error: err => {
        // this.toastr.error(err.error);
        this.router.navigateByUrl('/busy-times-list-employee');
      }
    })
  }
}
