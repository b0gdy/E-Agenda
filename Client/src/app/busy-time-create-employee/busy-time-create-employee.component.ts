import { Component, OnInit } from '@angular/core';
import {EmployeeService} from "../_services/employee.service";
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {NgForm} from "@angular/forms";
import {BusyTime} from "../_models/busyTimes";
import {BusyTimeService} from "../_services/busy-time.service";
import {Employee} from "../_models/employee";
import {UserService} from "../_services/user.service";
import {take} from "rxjs";
import {User} from "../_models/user";

@Component({
  selector: 'app-busy-time-create-employee',
  templateUrl: './busy-time-create-employee.component.html',
  styleUrls: ['./busy-time-create-employee.component.css']
})
export class BusyTimeCreateEmployeeComponent implements OnInit {

  model:any = {}
  busyTime: BusyTime = {} as BusyTime;
  minStartDate: Date = new Date();
  minEndDate: Date = new Date();
  maxEndDate: Date = new Date();
  startDate: Date = new Date();
  endDate: Date = new Date();
  startDateSelected: boolean = false;
  endDateSelected: boolean = false;
  busyTimes: BusyTime[] = [];
  employee: Employee = {} as Employee;
  user: User = {} as User;
  employeeFetched : boolean = false;

  constructor(private busyTimeService: BusyTimeService, private employeeService: EmployeeService, private router: Router, private toastr: ToastrService, private userService: UserService) {
    this.userService.currentUser$.pipe(take(1)).subscribe({
      next: user => {
        if(user) {
          this.user = user;
        }
      }
    });
  }

  ngOnInit(): void {
    this.employeeFetched = false;
    this.getEmployeeById(this.user.id);
  }

  getEmployeeById(id: number) {
    this.employeeService.getById(id).subscribe({
      next: (response: Employee) => {
        this.employee = response;
        this.employeeFetched = true;
      },
      error: error => {
        this.toastr.error(error.error);
      }
    })
  }

  create(model: NgForm) {
    if(this.validateDates(this.model.startDate, this.model.endDate)) {
      this.model.startDate = new Date(this.model.startDate);
      this.model.endDate = new Date(this.model.endDate);
      this.model.employeeId = this.employee.id;

      this.busyTimeService.create(this.model).subscribe({
        next: (response: BusyTime) => {
          this.router.navigateByUrl('/busy-times-list-employee');
        },
        error: err => {
          this.startDateSelected = false;
          this.endDateSelected = false;
          this.toastr.error(err.error);
          this.router.navigateByUrl('/busy-time-create-employee');
        },
        complete: () => {
          this.toastr.success("Busy time created!");
        }
      })
    }
  }

  validateDates(startDate: Date, endDate: Date) {
    this.minStartDate = new Date();
    this.minEndDate = new Date();
    this.minStartDate.setMinutes(this.minStartDate.getMinutes() + 15);
    this.minEndDate.setMinutes(this.startDate.getMinutes() + 15);
    startDate = new Date(startDate);
    endDate = new Date(endDate);
    if(startDate >= this.minStartDate) {
      if((endDate >= this.minEndDate) && (startDate.getFullYear() === endDate.getFullYear()) && (startDate.getMonth() === endDate.getMonth()) && (startDate.getDate() === endDate.getDate())){
        return true;
      } else {
        this.toastr.error("End date invalid!");
        return false;
      }
    } else {
      this.toastr.error("Start date invalid!");
      return false;
    }
  }

  selectStartDate(date: Date) {
    date = new Date(date);
    this.minStartDate = new Date();
    this.minStartDate.setMinutes(this.minStartDate.getMinutes() + 15);
    if(date >= this.minStartDate) {
      this.startDate = date;
      this.startDateSelected = true;
      this.toastr.info("Start date selected!");
    } else {
      this.startDateSelected = false;
      this.endDate = {} as Date;
      if(date < this.minStartDate) {
        this.toastr.error("Start date can't be sooner than 15 minutes!")
      } else {
        this.toastr.error("Start date invalid!")
      }
    }
    this.minEndDate = new Date(date);
    this.minEndDate.setMinutes(date.getMinutes() + 15);
  }

  selectEndDate(date: Date) {
    date = new Date(date);
    this.minEndDate = new Date(this.startDate);
    this.maxEndDate = new Date(this.startDate);
    this.minEndDate.setMinutes(this.startDate.getMinutes() + 15);
    this.maxEndDate.setHours(this.startDate.getHours() + 2);
    if((date >= this.minEndDate) && (date <= this.maxEndDate) && (this.startDate.getFullYear() === date.getFullYear()) && (this.startDate.getMonth() === date.getMonth()) && (this.startDate.getDate() === date.getDate())) {
      this.endDate = date;
      this.endDateSelected = true;
      this.toastr.info("End date selected!");
    } else {
      this.endDateSelected = false;
      if((date < this.startDate)) {
        this.toastr.error("End date before start date!")
      } else if((date < this.minEndDate)) {
        this.toastr.error("Busy time must be at least 15 minutes long!")
      } else if((date > this.maxEndDate)) {
        this.toastr.error("Busy time must be at last 2 hours long!")
      } else {
        this.toastr.error("End date invalid!")
      }
    }
  }



}
