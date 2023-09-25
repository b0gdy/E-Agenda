import {Component, Input, OnInit} from '@angular/core';
import {ActivatedRoute, Params, Router} from "@angular/router";
import {EmployeeService} from "../_services/employee.service";
import {Employee} from "../_models/employee";
import {ToastrService} from "ngx-toastr";
import {UserService} from "../_services/user.service";
import {take} from "rxjs";
import {User} from "../_models/user";
import {Status} from "../_models/status";

@Component({
  selector: 'app-employee-details',
  templateUrl: './employee-details.component.html',
  styleUrls: ['./employee-details.component.css']
})
export class EmployeeDetailsComponent implements OnInit {

  employeeId: number = {} as number;
  employee: Employee = {} as Employee;
  user: User = {} as User;
  fetched: boolean = false;

  constructor(private route: ActivatedRoute, private employeeService: EmployeeService, private toastr: ToastrService, private userService: UserService) {
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
        this.employeeId= params['id'];
        this.getById(this.employeeId);
      },
      error: error => {
        this.toastr.error(error.error);
      }
    })
  }

  getById(id: number) {
    this.employeeService.getById(id).subscribe({
      next: (response: Employee) => {
        this.employee = response;
        this.fetched = true;
      },
      error: error => {
        this.toastr.error(error.error);
        this.fetched = false;
      }
    })
  }

  updateEnabled(enabled: boolean) {
    this.employeeService.updateEnabled(this.employee.id, enabled).subscribe({
      next: (response: Employee) => {
        this.employee = response;
        this.fetched = true;
      },
      error: error => {
        this.toastr.error(error.error);
        this.fetched = false;
      },
      complete: () => {
        if (enabled) {
          this.toastr.success("Employee enabled!");
        } else {
          this.toastr.success("Employee disabled!");
        }
      }
    })
  }


}
